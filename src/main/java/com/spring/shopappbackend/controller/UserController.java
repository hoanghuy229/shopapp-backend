package com.spring.shopappbackend.controller;

import com.spring.shopappbackend.dto.*;
import com.spring.shopappbackend.exception.DataNotFoundException;
import com.spring.shopappbackend.exception.InvalidParamException;
import com.spring.shopappbackend.model.User;
import com.spring.shopappbackend.response.UserListResponse;
import com.spring.shopappbackend.response.UserResponse;
import com.spring.shopappbackend.service.ITokenService;
import com.spring.shopappbackend.service.ITwilioService;
import com.spring.shopappbackend.service.IUserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor
public class UserController {

    private final IUserService iUserService;
    private final ModelMapper modelMapper;
    private final ITwilioService iTwilioService;
    private final ITokenService iTokenService;

    @GetMapping("/admin/getAll")
    public ResponseEntity<UserListResponse> getAllUser(
            @RequestParam(defaultValue = "0",name = "page")  int page,
            @RequestParam(defaultValue = "12",name = "limit") int limit,
            @RequestParam(defaultValue = "",name = "keyword") String keyword){

        PageRequest pageRequest = PageRequest.of(page,limit, Sort.by("id").ascending());
        Page<UserResponse> userResponsePage = iUserService.findByKeyword(keyword,pageRequest);
        int totalPage = userResponsePage.getTotalPages();
        List<UserResponse> userResponses = userResponsePage.getContent();
        return ResponseEntity.ok(UserListResponse.builder().userResponses(userResponses).totalPages(totalPage).build());

    }


    @PostMapping("/register")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserDTO userDTO, BindingResult bindingResult){
        try{
            if(bindingResult.hasErrors()){
                List<String> errorMessage = bindingResult.getFieldErrors().stream()
                        .map(FieldError::getDefaultMessage).toList();

                return ResponseEntity.badRequest().body("failed!!!");
            }
            if(!userDTO.getPassword().equals(userDTO.getRePassword())){
                return ResponseEntity.badRequest().body("password not match");
            }
            iUserService.createUser(userDTO);
            return ResponseEntity.ok("create successfully");
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody UserLoginDTO userLoginDTO,
                                        @RequestHeader("User-Agent") String userAgent)  {
        try {
            String token = iUserService.login(userLoginDTO.getPhoneNumber(),userLoginDTO.getPassword());
            User user = iUserService.getUserDetailFromToken(token);
            iTokenService.addToken(user,token,isMobileDevice(userAgent));
            return ResponseEntity.ok(token);
        } catch (DataNotFoundException | InvalidParamException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isMobileDevice(String userAgent){
        return userAgent.toLowerCase().contains("mobile");// Kiểm tra User-Agent header để xác định thiết bị di động
    }

    @PostMapping("/details")
    public ResponseEntity<UserResponse> getUserDetail(@RequestHeader("Authorization") String token){
        try{
            String extractToken = token.substring(7);
            User user = iUserService.getUserDetailFromToken(extractToken);
            UserResponse userResponse = new UserResponse();
            modelMapper.map(user, userResponse);
            return ResponseEntity.ok().body(userResponse);
        }
        catch(Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/details/{id}")
    public ResponseEntity<UserResponse> updateUserDetails(@PathVariable("id") Long id,
                                                          @RequestHeader("Authorization") String token,
                                                          @RequestBody UpdateUserDTO updateUserDTO){
        try{
            String extractToken = token.substring(7);
            User user = iUserService.getUserDetailFromToken(extractToken); // xác thực user
            if(user.getId() != id){ // kiểm tra user đang sửa thông tin của mình hay của người khác
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); //403
            }
            User updateUser = iUserService.updateUser(id,updateUserDTO);
            UserResponse userResponse = new UserResponse();
            modelMapper.map(updateUser, userResponse);
            return ResponseEntity.ok().body(userResponse);
        }
        catch (Exception e){
           return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/admin/{id}")
    public ResponseEntity<?> adminDeleteUser(@PathVariable("id") Long id,
                                             @RequestHeader("Authorization") String token){
        try{
            String extractToken = token.substring(7);
            User admin = iUserService.getUserDetailFromToken(extractToken); // xác thực phải là admin không
            if(admin.getRole().getId() != 1){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); //403 không có quyền admin
            }
            iUserService.solfDeleteUser(id);
            return ResponseEntity.ok().body("successfully");
        }
        catch(Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/otp/forgetPassword")
    public ResponseEntity<?> forgetPassword(@RequestBody PhoneNumberDTO phoneNumberDTO) {
        try {
            String result = iUserService.checkPhoneNumber(phoneNumberDTO.getPhoneNumber());
            iTwilioService.sendOTP(phoneNumberDTO.getPhoneNumber());
            return ResponseEntity.ok().body("success");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/otp/validateOtp")
    public ResponseEntity<?> validateOtp(@RequestParam("otp") String otp, @RequestBody PhoneNumberDTO phoneNumberDTO) {
        try {
            String result = iTwilioService.validateOtp(otp, phoneNumberDTO.getPhoneNumber());
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/otp/resetPassword")
    public ResponseEntity<?> resetPassword(@RequestBody PasswordDTO passwordDTO,
                                           @RequestParam("phone_number") String phoneNumber,
                                           @RequestParam("otp")String otp) {
        try {
            if(!passwordDTO.getRePassword().equals(passwordDTO.getPassword())){
                return ResponseEntity.badRequest().body("password not match!!!");
            }
            String result = iTwilioService.validateOtp(otp, phoneNumber);
            iTwilioService.deleteOtp(otp);
            iUserService.resetPassword(passwordDTO,phoneNumber);
            return ResponseEntity.ok().body("change password success");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

}

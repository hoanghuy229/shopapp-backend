package com.spring.shopappbackend.controller;

import com.spring.shopappbackend.dto.UserDTO;
import com.spring.shopappbackend.dto.UserLoginDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/users")
public class UserController {

    @PostMapping("/register")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserDTO userDTO, BindingResult bindingResult){
        try{
            if(bindingResult.hasErrors()){
                List<String> errorMessage = bindingResult.getFieldErrors().stream()
                        .map(FieldError::getDefaultMessage).toList();

                return ResponseEntity.badRequest().body(errorMessage);
            }
            if(!userDTO.getPassword().equals(userDTO.getRePassword())){
                return ResponseEntity.badRequest().body("password not match");
            }
            return ResponseEntity.ok("create successfully");
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserLoginDTO userLoginDTO){
        return ResponseEntity.ok("token");
    }
}

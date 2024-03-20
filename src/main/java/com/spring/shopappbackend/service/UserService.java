package com.spring.shopappbackend.service;

import com.spring.shopappbackend.component.JwtTokenUtil;
import com.spring.shopappbackend.dto.PasswordDTO;
import com.spring.shopappbackend.dto.PhoneNumberDTO;
import com.spring.shopappbackend.dto.UpdateUserDTO;
import com.spring.shopappbackend.dto.UserDTO;
import com.spring.shopappbackend.exception.DataNotFoundException;
import com.spring.shopappbackend.exception.InvalidParamException;
import com.spring.shopappbackend.exception.PermissionException;
import com.spring.shopappbackend.model.Role;
import com.spring.shopappbackend.model.User;
import com.spring.shopappbackend.repository.RoleRepository;
import com.spring.shopappbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService{
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;


    @Override //register
    @Transactional
    public User createUser(UserDTO userDTO) throws DataNotFoundException, PermissionException {
        String phoneNumber = userDTO.getPhoneNumber();
        //check exist
        if(userRepository.existsByPhoneNumber(phoneNumber)){
            throw new DataIntegrityViolationException("phone number existed");
        }

        Role role = roleRepository.findById(userDTO.getRoleId()).orElseThrow(() -> new DataNotFoundException("Role not found"));
        if(role.getName().equals("ROLE_ADMIN")){
            throw new PermissionException("cannot register with role admin");
        }
        //convert from userDTO => user
        User newUser = User.builder()
                .fullName(userDTO.getFullName())
                .phoneNumber(userDTO.getPhoneNumber())
                .password(userDTO.getPassword())
                .address(userDTO.getAddress())
                .dateOfBirth(userDTO.getDateOfBirth())
                .facebookAccountId(userDTO.getFacebookAccountId())
                .googleAccountId(userDTO.getGoogleAccountId())
                .build();

        newUser.setActive(true);
        newUser.setRole(role);
        //check account id , if dont have require password
        if(userDTO.getFacebookAccountId() == 0 && userDTO.getGoogleAccountId() == 0){
            String password = userDTO.getPassword();
            String encodePassword = passwordEncoder.encode(password);
            newUser.setPassword(encodePassword);
        }
        return userRepository.save(newUser);
    }

    @Override
    public String login(String phoneNumber, String password) throws DataNotFoundException, InvalidParamException {
        Optional<User> user = userRepository.findByPhoneNumber(phoneNumber);
        if(user.isEmpty()){
            throw new DataNotFoundException("invalid information !!!");
        }
        User existUser = user.get();
        if(existUser.getFacebookAccountId() == 0 && existUser.getGoogleAccountId() == 0){
           if(!passwordEncoder.matches(password,existUser.getPassword())){
               throw new BadCredentialsException("invalid information !!!");
           }
        }

        Optional<Role> optionalRole = roleRepository.findById(existUser.getRole().getId());
        if(optionalRole.isEmpty()){
            throw new DataNotFoundException("role doesn't existed");
        }
        if(!existUser.isActive()){
            throw new DataNotFoundException("your account banned");
        }

        //store phoneNumber and Pass and role
        UsernamePasswordAuthenticationToken authenticationToken =  new UsernamePasswordAuthenticationToken(
                phoneNumber,password,existUser.getAuthorities());

        //authenticate user info
        authenticationManager.authenticate(authenticationToken);
        return jwtTokenUtil.generateToken(existUser); //create token JWT for user and return
    }

    @Override
    public User getUserDetailFromToken(String token) throws Exception{
        if(jwtTokenUtil.isTokenExpired(token)){
            throw new Exception("token is expired");
        }
        String phoneNumber = jwtTokenUtil.extractPhoneNumber(token);
        Optional<User> user = userRepository.findByPhoneNumber(phoneNumber);
        if(user.isPresent()){
            return user.get();
        }
        else{
            throw new Exception("user not found");
        }
    }

    @Override
    @Transactional
    public User updateUser(Long id, UpdateUserDTO updateUserDTO) throws Exception {
        // Find the existing user by userId
        User existUser = userRepository.findById(id).orElseThrow(() -> new DataNotFoundException("User not found"));

        // Update user information based on the DTO
        if (updateUserDTO.getFullName() != null) {
            existUser.setFullName(updateUserDTO.getFullName());
        }
        if (updateUserDTO.getAddress() != null) {
            existUser.setAddress(updateUserDTO.getAddress());
        }
        if (updateUserDTO.getDateOfBirth() != null) {
            existUser.setDateOfBirth(updateUserDTO.getDateOfBirth());
        }
        if (updateUserDTO.getFacebookAccountId() > 0) {
            existUser.setFacebookAccountId(updateUserDTO.getFacebookAccountId());
        }
        if (updateUserDTO.getGoogleAccountId() > 0) {
            existUser.setGoogleAccountId(updateUserDTO.getGoogleAccountId());
        }

        return userRepository.save(existUser);
    }

    public String checkPhoneNumber(String phoneNumber) throws Exception{
        boolean result = userRepository.existsByPhoneNumber(phoneNumber);
        if(!result){
            return "phone number doesn't existed !!!";
        }
        return "phone number founded !!!";
    }

    @Override
    public void resetPassword(PasswordDTO passwordDTO,String phoneNumber) throws Exception {
        // Lấy thông tin người dùng từ DTO
        User user = userRepository.findByPhoneNumber(phoneNumber).orElseThrow(() -> new DataNotFoundException("User not found"));
        String newPassword = passwordDTO.getPassword();
        if (user == null) {
            throw new Exception("User not found");
        }
        // Đặt mật khẩu mới cho người dùng và lưu vào cơ sở dữ liệu
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}

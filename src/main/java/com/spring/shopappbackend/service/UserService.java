package com.spring.shopappbackend.service;

import com.spring.shopappbackend.component.JwtTokenUtil;
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
        //store phoneNumber and Pass and role
        UsernamePasswordAuthenticationToken authenticationToken =  new UsernamePasswordAuthenticationToken(
                phoneNumber,password,existUser.getAuthorities());

        //authenticate user info
        authenticationManager.authenticate(authenticationToken);
        return jwtTokenUtil.generateToken(existUser); //create token JWT for user and return
    }
}

package com.spring.shopappbackend.service;

import com.spring.shopappbackend.dto.*;
import com.spring.shopappbackend.exception.DataNotFoundException;
import com.spring.shopappbackend.exception.InvalidParamException;
import com.spring.shopappbackend.exception.PermissionException;
import com.spring.shopappbackend.model.OneTimePassword;
import com.spring.shopappbackend.model.User;
import com.spring.shopappbackend.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface IUserService {
    User createUser(UserDTO userDTO) throws DataNotFoundException, PermissionException;

    String login(String phoneNumber, String password) throws DataNotFoundException, InvalidParamException;

    User getUserDetailFromToken(String token) throws Exception;

    void solfDeleteUser(Long id) throws Exception;

    User updateUser(Long id, UpdateUserDTO updateUserDTO) throws Exception;

    String checkPhoneNumber(String phoneNumber) throws Exception;

    void resetPassword(PasswordDTO passwordDTO, String phoneNumber) throws Exception;

    Page<UserResponse> findByKeyword(String keyword, PageRequest pageRequest);
    String GoogleLogin(SocialAccountDTO socialAccountDTO) throws DataNotFoundException, PermissionException, InvalidParamException;
}

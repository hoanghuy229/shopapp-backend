package com.spring.shopappbackend.service;

import com.spring.shopappbackend.dto.PasswordDTO;
import com.spring.shopappbackend.dto.PhoneNumberDTO;
import com.spring.shopappbackend.dto.UpdateUserDTO;
import com.spring.shopappbackend.dto.UserDTO;
import com.spring.shopappbackend.exception.DataNotFoundException;
import com.spring.shopappbackend.exception.InvalidParamException;
import com.spring.shopappbackend.exception.PermissionException;
import com.spring.shopappbackend.model.OneTimePassword;
import com.spring.shopappbackend.model.User;

public interface IUserService {
    User createUser(UserDTO userDTO) throws DataNotFoundException, PermissionException;
    String login(String phoneNumber, String password) throws DataNotFoundException, InvalidParamException;

    User getUserDetailFromToken(String token) throws Exception;

    User updateUser(Long id, UpdateUserDTO updateUserDTO) throws Exception;
    String checkPhoneNumber(String phoneNumber) throws Exception;
    void resetPassword(PasswordDTO passwordDTO, String phoneNumber) throws Exception;
}

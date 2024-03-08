package com.spring.shopappbackend.service;

import com.spring.shopappbackend.dto.UserDTO;
import com.spring.shopappbackend.exception.DataNotFoundException;
import com.spring.shopappbackend.exception.InvalidParamException;
import com.spring.shopappbackend.exception.PermissionException;
import com.spring.shopappbackend.model.User;

public interface IUserService {
    User createUser(UserDTO userDTO) throws DataNotFoundException, PermissionException;
    String login(String phoneNumber, String password) throws DataNotFoundException, InvalidParamException;
}

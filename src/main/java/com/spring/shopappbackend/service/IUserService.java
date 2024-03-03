package com.spring.shopappbackend.service;

import com.spring.shopappbackend.dto.UserDTO;
import com.spring.shopappbackend.exception.DataNotFoundException;
import com.spring.shopappbackend.model.User;

public interface IUserService {
    User createUser(UserDTO userDTO) throws DataNotFoundException;
    String login(String phoneNumber, String password);
}

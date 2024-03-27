package com.spring.shopappbackend.service;

import com.spring.shopappbackend.exception.DataNotFoundException;
import com.spring.shopappbackend.model.User;

public interface ITokenService {
    void addToken(User user, String token, boolean isMobileDevice);
}

package com.spring.shopappbackend.repository;

import com.spring.shopappbackend.model.Token;
import com.spring.shopappbackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TokenRepository extends JpaRepository<Token,Long> {
    List<Token> findByUser(User user);
    Token findByToken(String token);
}

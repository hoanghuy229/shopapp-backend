package com.spring.shopappbackend.service;

import com.spring.shopappbackend.exception.DataNotFoundException;
import com.spring.shopappbackend.model.Token;
import com.spring.shopappbackend.model.User;
import com.spring.shopappbackend.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TokenService implements ITokenService{
    private static final int MAX_TOKENS = 3;
    @Value("${jwt.expiration}")
    private Long expiration; //save to environment variable (application.yml)

    private final TokenRepository tokenRepository;

    @Override
    @Transactional
    public void addToken(User user, String token, boolean isMobileDevice) {
        List<Token> userTokens = tokenRepository.findByUser(user);
        int tokenCount = userTokens.size();
        if(tokenCount >= MAX_TOKENS){// Số lượng token vượt 3,sẽ xóa 1 token cũ
            //lấy ra token không phải là mobile
            boolean hasNonMobileToken = !userTokens.stream().allMatch(Token::isMobile);
            Token tokenDelete;
            if(hasNonMobileToken){//xóa token đầu tiên không phải là mobile
                tokenDelete = userTokens.stream().filter(userToken -> !userToken.isMobile()).findFirst().orElse(userTokens.get(0));
            }else{
                //nếu tất cả token là mobile thì xóa cái đầu tiên
                tokenDelete = userTokens.get(0);
            }
            tokenRepository.delete(tokenDelete);
        }
        long expirationInSeconds = expiration;
        LocalDateTime expirationDateTime = LocalDateTime.now().plusSeconds(expirationInSeconds);
        // Tạo mới một token cho người dùng trong CSDL
        Token newToken = Token.builder()
                .user(user)
                .token(token)
                .revoked(false)
                .expired(false)
                .tokenType("Bearer")
                .expirationDate(expirationDateTime)
                .isMobile(isMobileDevice)
                .build();
        tokenRepository.save(newToken);
    }

}

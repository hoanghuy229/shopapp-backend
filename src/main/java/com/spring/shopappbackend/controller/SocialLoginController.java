package com.spring.shopappbackend.controller;

import com.spring.shopappbackend.component.JwtTokenUtil;
import com.spring.shopappbackend.dto.SocialAccountDTO;
import com.spring.shopappbackend.exception.DataNotFoundException;
import com.spring.shopappbackend.exception.InvalidParamException;
import com.spring.shopappbackend.model.User;
import com.spring.shopappbackend.service.ITokenService;
import com.spring.shopappbackend.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("${api.prefix}/social")
@RequiredArgsConstructor
public class SocialLoginController {
    private final ITokenService iTokenService;
    private final JwtTokenUtil jwtTokenUtil;
    private final IUserService iUserService;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String googleRedirectUri;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String googleClientSecret;


    @PostMapping("/google-login")
    public ResponseEntity<?> googleLogin(
                                        @RequestBody SocialAccountDTO socialAccountDTO,
                                        BindingResult bindingResult,
                                        @RequestHeader("User-Agent") String userAgent){
        try{
            if(bindingResult.hasErrors()) {
                List<String> errorMessage = bindingResult.getFieldErrors().stream()
                        .map(FieldError::getDefaultMessage).toList();

                return ResponseEntity.badRequest().body(errorMessage);
            }
            String token  = iUserService.GoogleLogin(socialAccountDTO);
            User user = iUserService.getUserDetailFromToken(token);
            iTokenService.addToken(user,token,isMobileDevice(userAgent));
            return ResponseEntity.ok().body(token);
        }
        catch (DataNotFoundException | InvalidParamException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isMobileDevice(String userAgent){
        return userAgent.toLowerCase().contains("mobile");// Kiểm tra User-Agent header để xác định thiết bị di động
    }

}

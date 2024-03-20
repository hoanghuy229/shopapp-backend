package com.spring.shopappbackend.service;

import com.spring.shopappbackend.dto.PhoneNumberDTO;
import com.spring.shopappbackend.model.OneTimePassword;

public interface ITwilioService {
    String sendOTP(String phoneNumber);
    String validateOtp(String otp,String phoneNumber) throws Exception;

    void deleteOtp(String otpCode);
}

package com.spring.shopappbackend.service;

import com.spring.shopappbackend.model.OneTimePassword;
import com.spring.shopappbackend.repository.OtpRepository;
import com.twilio.Twilio;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class TwilioService implements ITwilioService {
    @Value("${twilio.accountSid}")
    private String accountSid;
    @Value("${twilio.authToken}")
    private String authToken;
    @Value("${twilio.phoneNumber}")
    private String trialNumber;
    private final OtpRepository otpRepository;

    public void sendOTP(String phoneNumber) {
        String format = "+84" + phoneNumber.substring(1);
        PhoneNumber to = new PhoneNumber(format);
        PhoneNumber from = new PhoneNumber(trialNumber);
        String otp = generateOTP();
        int otpInt = Integer.parseInt(otp);

        Twilio.init(accountSid, authToken);
        Message message = Message.creator(to, from, "Your OTP is: " + otp).create();

        // Lưu OTP cùng với thời gian hết hạn vào cơ sở dữ liệu
        OneTimePassword newOtp = new OneTimePassword(phoneNumber, otpInt, calculateExpiryTime());
        OneTimePassword oldOtp = otpRepository.findByPhoneNumber(phoneNumber);
        if(oldOtp != null){
            otpRepository.delete(oldOtp);
        }
        otpRepository.save(newOtp);

    }

    @Override
    public String validateOtp(String otp, String phoneNumber) throws Exception {
        OneTimePassword userOtp = otpRepository.findByPhoneNumber(phoneNumber);
        int intOtp = Integer.parseInt(otp);
        if (userOtp == null) {
            throw new Exception("Cannot find phone number");
        }
        if (userOtp.getOtpCode() != intOtp) {
            throw new Exception("Invalid OTP");
        }
        // Kiểm tra xem OTP có hết hạn không
        if (userOtp.getExpiryTime().isBefore(LocalDateTime.now())) {
            deleteOtp(otp);
            throw new Exception("OTP has expired");
        }
        return "Valid OTP!!!";
    }
    @Override
    public void deleteOtp(String otpCode){
        int otpInt = Integer.parseInt(otpCode);
        OneTimePassword otp = otpRepository.findByOtpCode(otpInt);
        otpRepository.delete(otp);
    }


    // Tính thời gian hết hạn cho OTP (ví dụ: 5 phút sau)
    private LocalDateTime calculateExpiryTime() {
        return LocalDateTime.now().plusMinutes(5);
    }

    // Tạo OTP gồm 6 chữ số
    private String generateOTP() {
        return new DecimalFormat("000000").format(new Random().nextInt(999999));
    }
}

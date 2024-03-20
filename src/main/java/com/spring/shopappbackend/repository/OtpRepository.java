package com.spring.shopappbackend.repository;

import com.spring.shopappbackend.model.OneTimePassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OtpRepository extends JpaRepository<OneTimePassword,Integer> {
    OneTimePassword findByPhoneNumber(String phoneNumber);
    OneTimePassword findByOtpCode(int otpCode);
}

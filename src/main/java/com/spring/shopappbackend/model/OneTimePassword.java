package com.spring.shopappbackend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Table(name = "otp")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OneTimePassword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "otp_code")
    private int otpCode;

    @Column(name = "expiry_time")
    private LocalDateTime expiryTime;

    public OneTimePassword(String phoneNumber, int otpCode, LocalDateTime expiryTime) {
        this.phoneNumber = phoneNumber;
        this.otpCode = otpCode;
        this.expiryTime = expiryTime;
    }
}

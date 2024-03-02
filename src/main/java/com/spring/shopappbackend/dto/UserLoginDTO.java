package com.spring.shopappbackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserLoginDTO {

    @JsonProperty("phone_number")
    @NotBlank(message = "phone number cannot be emty")
    private String phoneNumber;

    @NotBlank(message = "password cannot be emty")
    private String password;
}

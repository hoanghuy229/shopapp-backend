package com.spring.shopappbackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserLoginDTO {

    @JsonProperty("phone_number")
    @NotBlank(message = "phone number cannot be emty")
    private String phoneNumber;

    @NotBlank(message = "password cannot be emty")
    private String password;

    @Min(value = 1,message = "enter your role's Id")
    @JsonProperty("role_id")
    private Long roleId;
}

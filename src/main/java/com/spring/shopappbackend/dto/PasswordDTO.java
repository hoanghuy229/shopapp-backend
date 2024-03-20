package com.spring.shopappbackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordDTO {
    @NotBlank(message = "password cannot be emty")
    @JsonProperty("password")
    private String password;

    @NotBlank(message = "re_password cannot be emty")
    @JsonProperty("re_password")
    private String rePassword;
}

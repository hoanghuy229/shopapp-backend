package com.spring.shopappbackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDTO {
    @JsonProperty("fullname")
    private String fullName;

    @JsonProperty("phone_number")
    @NotBlank(message = "phone number cannot be emty")
    private String phoneNumber;

    private String address;

    @NotBlank(message = "password cannot be emty")
    private String password;

    @JsonProperty("re_password")
    private String rePassword;

    @JsonProperty("date_of_birth")
    private String dateOfBirth;

    @JsonProperty("facebook_account_id")
    private int facebookAccountId;

    @JsonProperty("google_account_id")
    private int googleAccountId;

    @NotNull(message = "role id is required")
    @JsonProperty("role_id")
    private Long roleId;
}

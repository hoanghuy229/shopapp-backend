package com.spring.shopappbackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SocialAccountDTO {
    @JsonProperty("fullname")
    private String fullName;

    @JsonProperty("google_account_id")
    private String googleAccountId;

    @NotNull(message = "role id is required")
    @JsonProperty("role_id")
    private Long roleId;

    @JsonProperty("email")
    private String email;

}

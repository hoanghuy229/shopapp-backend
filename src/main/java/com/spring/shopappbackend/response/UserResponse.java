package com.spring.shopappbackend.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.spring.shopappbackend.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("fullname")
    private String fullname;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("address")
    private String address;

    @JsonProperty("is_active")
    private boolean active;

    @JsonProperty("date_of_birth")
    private Date DateOfBirth;

    @JsonProperty("facebook_account_id")
    private String faceBookAccountId;

    @JsonProperty("google_account_id")
    private String googleAccountId;

    @JsonProperty("role")
    private Role role;

}

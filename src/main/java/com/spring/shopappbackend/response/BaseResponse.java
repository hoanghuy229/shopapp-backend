package com.spring.shopappbackend.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.MappedSuperclass;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@MappedSuperclass
public class BaseResponse {
    @JsonProperty("create_at")
    private LocalDateTime createdAt;

    @JsonProperty("update_at")
    private LocalDateTime updatedAt;

}

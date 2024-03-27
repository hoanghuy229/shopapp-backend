package com.spring.shopappbackend.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.spring.shopappbackend.model.User;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CommentResponse extends BaseResponse{
    @JsonProperty("id")
    private Long id;

    @JsonProperty("user_response")
    private UserResponse userResponse;

    @JsonProperty("product_response")
    private ProductResponse productResponse;

    @JsonProperty("content")
    private String content;

    @JsonProperty("points")
    private Float points;
}

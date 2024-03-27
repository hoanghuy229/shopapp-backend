package com.spring.shopappbackend.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentListResponse {
    private List<CommentResponse> commentResponses;
    private int totalPages;
}

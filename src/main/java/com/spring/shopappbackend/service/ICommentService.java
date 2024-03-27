package com.spring.shopappbackend.service;

import com.spring.shopappbackend.dto.CommentDTO;
import com.spring.shopappbackend.model.Comment;
import com.spring.shopappbackend.response.CommentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICommentService {
    void insertComment(CommentDTO commentDTO);
    void deleteComment(Long commentId);
    void updateComment(CommentDTO commentDTO,Long commentId);
    Comment getById(Long id);
    List<Comment> getCommentByUser(Long userId);
    Page<CommentResponse> getCommentByProduct(Long productId, PageRequest pageRequest);

}

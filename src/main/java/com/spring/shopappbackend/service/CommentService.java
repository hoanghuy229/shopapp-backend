package com.spring.shopappbackend.service;

import com.spring.shopappbackend.dto.CommentDTO;
import com.spring.shopappbackend.model.Comment;
import com.spring.shopappbackend.model.Product;
import com.spring.shopappbackend.model.User;
import com.spring.shopappbackend.repository.CommentRepository;
import com.spring.shopappbackend.repository.ProductRepository;
import com.spring.shopappbackend.repository.UserRepository;
import com.spring.shopappbackend.response.CommentResponse;
import com.spring.shopappbackend.response.ProductResponse;
import com.spring.shopappbackend.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DateTimeException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService implements ICommentService{
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public void insertComment(CommentDTO commentDTO) {
        User user = userRepository.findById(commentDTO.getUserId()).orElseThrow(() -> new RuntimeException("user not found"));
        Product product = productRepository.findById(commentDTO.getProductId()).orElseThrow(() -> new RuntimeException("product not found"));
        Comment comment = Comment.builder()
                .product(product)
                .user(user)
                .content(commentDTO.getContent())
                .points(commentDTO.getPoints())
                .build();

        commentRepository.save(comment);
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    @Override
    @Transactional
    public void updateComment(CommentDTO commentDTO, Long commentId) {
        Comment existComment = commentRepository.findById(commentId).orElseThrow(() -> new DateTimeException("cannot find"));
        existComment.setContent(commentDTO.getContent());
        existComment.setPoints(commentDTO.getPoints());
        commentRepository.save(existComment);
    }

    @Override
    public Comment getById(Long id) {
        return commentRepository.findById(id).orElseThrow(() -> new DateTimeException("cannot find"));
    }

    @Override
    public List<Comment> getCommentByUser(Long userId) {
        return commentRepository.findByUserId(userId);
    }
    @Override
    public Page<CommentResponse> getCommentByProduct(Long productId, PageRequest pageRequest) {
        Page<Comment> comments = commentRepository.findByProductId(productId,pageRequest);
        return comments.map(comment -> {
            CommentResponse commentResponse = modelMapper.map(comment, CommentResponse.class);
            User user = comment.getUser();
            Product product = comment.getProduct();
            commentResponse.setUserResponse(modelMapper.map(user,UserResponse.class));
            commentResponse.setProductResponse(modelMapper.map(product, ProductResponse.class));
            return commentResponse;
        });
    }
}

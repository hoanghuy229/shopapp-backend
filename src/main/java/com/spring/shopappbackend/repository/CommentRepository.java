package com.spring.shopappbackend.repository;

import com.spring.shopappbackend.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findByUserId(Long userId);
    Page<Comment> findByProductId(Long productId, Pageable pageable);
}

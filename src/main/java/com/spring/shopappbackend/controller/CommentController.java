package com.spring.shopappbackend.controller;

import com.spring.shopappbackend.dto.CommentDTO;
import com.spring.shopappbackend.model.Comment;
import com.spring.shopappbackend.model.User;
import com.spring.shopappbackend.response.CommentListResponse;
import com.spring.shopappbackend.response.CommentResponse;
import com.spring.shopappbackend.response.ProductResponse;
import com.spring.shopappbackend.service.ICommentService;
import com.spring.shopappbackend.service.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/comments")
public class CommentController {
    private final ICommentService iCommentService;
    private final IUserService iUserService;

    @GetMapping()
    public ResponseEntity<CommentListResponse> getCommentOfProduct(
            @RequestParam(defaultValue = "0",name = "page") int page,
            @RequestParam(defaultValue = "12",name = "limit") int limit,
            @RequestParam(defaultValue = "0",name = "product_id") Long productId){

        PageRequest pageRequest = PageRequest.of(page,limit, Sort.by("id").descending());
        Page<CommentResponse> commentResponsePage = iCommentService.getCommentByProduct(productId,pageRequest);
        int totalPages = commentResponsePage.getTotalPages();
        List<CommentResponse> comments = commentResponsePage.getContent();
        return ResponseEntity.ok(CommentListResponse.builder().commentResponses(comments).totalPages(totalPages).build());

    }

    @PostMapping()
    public ResponseEntity<?> createComment(@Valid @RequestBody CommentDTO commentDTO,
                                           BindingResult bindingResult,
                                           @RequestHeader("Authorization") String token) throws Exception {
        if(bindingResult.hasErrors()){
            List<String> errorMessage = bindingResult.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage).toList();
            return ResponseEntity.badRequest().body(errorMessage);
        }
        String extractToken = token.substring(7);
        User user =  iUserService.getUserDetailFromToken(extractToken);
        if(!Objects.equals(user.getId(), commentDTO.getUserId())){
            return ResponseEntity.badRequest().body("cannot comment for another!!!");
        }
        iCommentService.insertComment(commentDTO);
        return ResponseEntity.ok("comment success");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable("id") Long id,
                                                @RequestHeader("Authorization") String token) throws Exception {

        String extractToken = token.substring(7);
        User user =  iUserService.getUserDetailFromToken(extractToken);
        Comment comment = iCommentService.getById(id);
        if(!Objects.equals(user.getId(), comment.getUser().getId())){
            return ResponseEntity.badRequest().body("cannot delete comment for another!!!");
        }
        iCommentService.deleteComment(id);
        return ResponseEntity.ok("delete comment with id "+id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateComment(@PathVariable("id") Long commentId,
                                           @Valid @RequestBody CommentDTO commentDTO,
                                           BindingResult bindingResult,
                                           @RequestHeader("Authorization") String token) throws Exception {

        if(bindingResult.hasErrors()){
            List<String> errorMessage = bindingResult.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage).toList();
            return ResponseEntity.badRequest().body(errorMessage);
        }
        String extractToken = token.substring(7);
        User user =  iUserService.getUserDetailFromToken(extractToken);
        if(!Objects.equals(user.getId(), commentDTO.getUserId())){
            return ResponseEntity.badRequest().body("cannot update comment for another!!!");
        }
        iCommentService.updateComment(commentDTO,commentId);
        return ResponseEntity.ok("success");
    }
}

package com.spring.shopappbackend.controller;

import com.spring.shopappbackend.dto.CategoryDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/categories")
public class CategoryController {
    @GetMapping() //http://localhost:8080/api/v1/categories?page=1&limit=10
    public ResponseEntity<String> getAllCategories(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ){
        return ResponseEntity.ok(String.format("getall cate,page = %d,limit = %d",page,limit));
    }

    @PostMapping()//neu tham so truyen vao la 1 doi tuong ? => Data Transfer Object = Request Object
    public ResponseEntity<?> createCategories(@Valid @RequestBody CategoryDTO categoryDTO, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            List<String> errorMessage = bindingResult.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
            return ResponseEntity.badRequest().body(errorMessage);
        }
        return ResponseEntity.ok("create"+categoryDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateCategory(@PathVariable Long id){
        return ResponseEntity.ok("update "+id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id){
        return ResponseEntity.ok("delete "+id);
    }
}

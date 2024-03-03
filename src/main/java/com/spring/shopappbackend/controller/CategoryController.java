package com.spring.shopappbackend.controller;

import com.spring.shopappbackend.dto.CategoryDTO;
import com.spring.shopappbackend.model.Category;
import com.spring.shopappbackend.service.ICategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final ICategoryService iCategoryService;



    @PostMapping()//neu tham so truyen vao la 1 doi tuong ? => Data Transfer Object = Request Object
    public ResponseEntity<?> createCategories(@Valid @RequestBody CategoryDTO categoryDTO,
                                              BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            List<String> errorMessage = bindingResult.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage).toList();
            return ResponseEntity.badRequest().body(errorMessage);
        }
        iCategoryService.createCategory(categoryDTO);
        return ResponseEntity.ok("create category successfully!!!");
    }

    @GetMapping() //http://localhost:8080/api/v1/categories?page=1&limit=10
    public ResponseEntity<List<Category>> getAllCategories(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ){
        List<Category> categories = iCategoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }


    @PutMapping("/{id}")
    public ResponseEntity<String> updateCategory(@PathVariable Long id,@Valid @RequestBody CategoryDTO categoryDTO){
        iCategoryService.updateCategory(id,categoryDTO);
        return ResponseEntity.ok("update category successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id){
        iCategoryService.deleteCategory(id);
        return ResponseEntity.ok("delete category with id "+id);
    }
}

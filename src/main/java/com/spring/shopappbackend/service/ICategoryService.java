package com.spring.shopappbackend.service;

import com.spring.shopappbackend.dto.CategoryDTO;
import com.spring.shopappbackend.model.Category;

import java.util.List;

public interface ICategoryService {
    Category createCategory(CategoryDTO categoryDTO);
    Category getCategoryById(long id);
    List<Category> getAllCategories();
    Category updateCategory(long id, CategoryDTO categoryDTO);
    void deleteCategory(long id);
}

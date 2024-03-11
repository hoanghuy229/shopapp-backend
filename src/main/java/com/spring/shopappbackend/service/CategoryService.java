package com.spring.shopappbackend.service;

import com.spring.shopappbackend.dto.CategoryDTO;
import com.spring.shopappbackend.model.Category;
import com.spring.shopappbackend.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService{
    private final CategoryRepository categoryRepository;


    @Override
    @Transactional
    public Category createCategory(CategoryDTO categoryDTO) {
        Category newCate = Category.builder().name(categoryDTO.getName()).build();
        return categoryRepository.save(newCate);
    }

    @Override
    public Category getCategoryById(long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("category not found"));
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    @Transactional
    public Category updateCategory(long id, CategoryDTO categoryDTO) {
        Category existCategory = getCategoryById(id);
        existCategory.setName(categoryDTO.getName());
        categoryRepository.save(existCategory);
        return existCategory;
    }

    @Override
    @Transactional
    public void deleteCategory(long id) {
        categoryRepository.deleteById(id);
    }
}

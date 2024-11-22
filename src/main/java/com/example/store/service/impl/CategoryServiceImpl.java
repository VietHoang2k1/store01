package com.example.store.service.impl;

import com.example.store.model.Category;
import com.example.store.repository.CategoryRepository;
import com.example.store.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;
    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }
    private String successMessage;
    private String errorMessage;
    @Override
    public Category save(Category category) {
        try {
            Category savedCategory = categoryRepository.save(category);
            successMessage = "Danh mục đã được lưu thành công!";
            return savedCategory;
        } catch (Exception e) {
            errorMessage = "Lỗi khi lưu danh mục: " + e.getMessage();
            throw new RuntimeException(errorMessage);
        }
    }
    @Override
    public Category findById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }
    @Override
    public void deleteById(Long id) {
        try {
            categoryRepository.deleteById(id);
            successMessage = "Danh mục đã được xóa thành công!";
        } catch (Exception e) {
            errorMessage = "Lỗi khi xóa danh mục: " + e.getMessage();
            throw new RuntimeException(errorMessage);
        }
    }

    @Override
    public String getSuccessMessage() {
        return successMessage;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }
}
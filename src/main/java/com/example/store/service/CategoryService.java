package com.example.store.service;
import com.example.store.model.Category;
import com.example.store.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
public interface CategoryService {
    List<Category> findAll();
    Category save(Category category);
    Category findById(Long id);
    void deleteById(Long id);
    // Phương pháp phản hồi
    String getSuccessMessage();
    String getErrorMessage();

}

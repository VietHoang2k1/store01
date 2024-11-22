package com.example.store.service;
import com.example.store.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProductService {
    Page<Product> findProductsByCategoryId(Long categoryId, Pageable pageable);
    Page<Product> getProductsByPage(Pageable pageable);
    List<Product> getAllProducts();
    Product getProductById(Long id);
    Product saveProduct(Product product, MultipartFile imageFile) throws IOException;
    void deleteProduct(Long id);
    Page<Product> searchProducts(String keyword, Pageable pageable);
    List<Product> getTopSellingProducts();
    String getSuccessMessage();
    String getErrorMessage();
    List<Product> getTopProducts();

}
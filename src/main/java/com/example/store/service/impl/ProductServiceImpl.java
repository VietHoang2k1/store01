package com.example.store.service.impl;
import com.example.store.model.Product;
import com.example.store.repository.ProductRepository;
import com.example.store.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;
    private String successMessage;
    private String errorMessage;
    private final String uploadDir = "uploads/";
@Override
public Page<Product> getProductsByPage(Pageable pageable) {
    return productRepository.findAll(pageable); // Lấy sản phẩm theo trang
}
    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);



    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    @Override
    public Page<Product> searchProducts(String keyword, Pageable pageable) {
        logger.info("Search products with keyword: {}", keyword);
        if (keyword == null || keyword.trim().isEmpty()) {
            return productRepository.findAll(pageable);
        }
        return productRepository.searchProducts(keyword, pageable);
    }

    @Override
    public Page<Product> findProductsByCategoryId(Long categoryId, Pageable pageable) {
        // Gọi phương thức repository để tìm kiếm sản phẩm theo categoryId
        return productRepository.findByCategoryId(categoryId, pageable);
    }
    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }
    @Override
    public Product saveProduct(Product product, MultipartFile imageFile) throws IOException {
        try {
            if (imageFile != null && !imageFile.isEmpty()) {
                Path uploadPath = Paths.get(uploadDir);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
                Path filePath = uploadPath.resolve(fileName);
                Files.write(filePath, imageFile.getBytes());
                product.setImageUrl(fileName);
            }
            Product savedProduct = productRepository.save(product);
            successMessage = "Sản phẩm đã được lưu thành công!";
            return savedProduct;
        } catch (Exception e) {
            errorMessage = "Lỗi khi lưu sản phẩm: " + e.getMessage();
            throw new RuntimeException(errorMessage);
        }
    }
    @Override
    public List<Product> getTopProducts() {
        return productRepository.findTop5ByOrderBySalesDesc();
    }

    @Override
    public List<Product> getTopSellingProducts() {
        // Lấy top 5 sản phẩm bán chạy nhất (có thể điều chỉnh số lượng tùy ý)
        return productRepository.findTopSellingProducts(PageRequest.of(0, 5));
    }
    @Override
    public void deleteProduct(Long id) {
        try {
            productRepository.deleteById(id);
            successMessage = "Sản phẩm đã được xóa thành công!";
        } catch (Exception e) {
            errorMessage = "Lỗi khi xóa sản phẩm: " + e.getMessage();
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

package com.example.store.controller;

import com.example.store.model.Category;
import com.example.store.model.Order;
import com.example.store.model.Product;
import com.example.store.repository.CategoryRepository;
import com.example.store.service.CategoryService;
import com.example.store.service.OrderService;
import com.example.store.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private OrderService orderService;
    // Hiển thị trang chi tiết sản phẩm
    @GetMapping("/{id}")
    public String getProductDetails(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Product product = productService.getProductById(id);
        if (product == null) {
            redirectAttributes.addFlashAttribute("toastMessage", "Không tìm thấy sản phẩm để xem chi tiết!");
            redirectAttributes.addFlashAttribute("toastType", "error");
            return "redirect:/products/cards"; // Quay lại trang danh sách sản phẩm nếu không tìm thấy
        }
        model.addAttribute("product", product);
        return "/views/product_details"; // Đảm bảo file `product_details.html` tồn tại
    }


    @GetMapping
    public String getProductList(@RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "") String keyword,
                                 Model model) {
        Pageable pageable = PageRequest.of(page, 4);
        Page<Product> productPage = keyword.isEmpty()
                ? productService.getProductsByPage(pageable)
                : productService.searchProducts(keyword, pageable);
        for (Product product : productPage.getContent()) {
            if (product.getCategory() == null) {
                product.setCategory(new Category(0L, "No Category"));
            }
        }
        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        return "/admin/product_list";
    }
    @GetMapping("/cards")
    public String showProductCards(@RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "") String keyword,
                                   @RequestParam(required = false) Long categoryId,
                                   Model model) {
        int pageSize = 8; // Số sản phẩm trên mỗi trang
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Product> productPage;

        if (categoryId != null) {
            productPage = productService.findProductsByCategoryId(categoryId, pageable);
        } else if (!keyword.isEmpty()) {
            productPage = productService.searchProducts(keyword, pageable);
        } else {
            productPage = productService.getProductsByPage(pageable);
        }

        List<Product> topProducts = productService.getTopProducts();

        // Truyền danh sách vào model
        model.addAttribute("topProducts", topProducts);
        // Lấy danh sách danh mục
        List<Category> categories = categoryService.findAll();

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("categories", categories);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("keyword", keyword);

        return "/views/product_cards";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);
        model.addAttribute("product", new Product());
        return "/admin/product_form";
    }

    @PostMapping
    public String createProduct(@ModelAttribute Product product,
                                @RequestParam("imageFile") MultipartFile imageFile,
                                RedirectAttributes redirectAttributes) {
        try {
            productService.saveProduct(product, imageFile);
            redirectAttributes.addFlashAttribute("toastMessage", "Sản phẩm đã được thêm thành công!");
            redirectAttributes.addFlashAttribute("toastType", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("toastMessage", "Có lỗi xảy ra khi thêm sản phẩm!");
            redirectAttributes.addFlashAttribute("toastType", "error");
        }
        return "redirect:/products";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Product product = productService.getProductById(id);
        if (product == null) {
            redirectAttributes.addFlashAttribute("toastMessage", "Không tìm thấy sản phẩm để sửa!");
            redirectAttributes.addFlashAttribute("toastType", "error");
            return "redirect:/products";
        }
        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);
        model.addAttribute("product", product);
        return "/admin/product_form";
    }

    @PostMapping("/update/{id}")
    public String updateProduct(@PathVariable Long id,
                                @ModelAttribute Product product,
                                @RequestParam("imageFile") MultipartFile imageFile,
                                RedirectAttributes redirectAttributes) {
        try {
            product.setId(id);
            productService.saveProduct(product, imageFile);
            redirectAttributes.addFlashAttribute("toastMessage", "Sản phẩm đã được cập nhật thành công!");
            redirectAttributes.addFlashAttribute("toastType", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("toastMessage", "Có lỗi xảy ra khi cập nhật sản phẩm!");
            redirectAttributes.addFlashAttribute("toastType", "error");
        }
        return "redirect:/products";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            productService.deleteProduct(id);
            redirectAttributes.addFlashAttribute("toastMessage", "Sản phẩm đã được xóa thành công!");
            redirectAttributes.addFlashAttribute("toastType", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("toastMessage", "Không thể xóa sản phẩm. Có lỗi xảy ra!");
            redirectAttributes.addFlashAttribute("toastType", "error");
        }
        return "redirect:/products";
    }

    @PostMapping("/add-to-cart/{productId}")
    public String addToCart(@PathVariable Long productId, HttpSession session, RedirectAttributes redirectAttributes) {
        List<Product> cart = (List<Product>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
            session.setAttribute("cart", cart);
        }

        Product product = productService.getProductById(productId);
        if (product != null) {
            cart.add(product);
            redirectAttributes.addFlashAttribute("toastMessage", "Sản phẩm đã được thêm vào giỏ hàng!");
            redirectAttributes.addFlashAttribute("toastType", "success");
        } else {
            redirectAttributes.addFlashAttribute("toastMessage", "Không thể thêm sản phẩm vào giỏ hàng.");
            redirectAttributes.addFlashAttribute("toastType", "error");
        }

        return "redirect:/products/cards";
    }
    // Hiển thị trang thanh toán
    @GetMapping("/checkout")
    public String checkoutPage(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        List<Product> cart = (List<Product>) session.getAttribute("cart");
        if (cart == null || cart.isEmpty()) {
            redirectAttributes.addFlashAttribute("toastMessage", "Giỏ hàng của bạn đang trống!");
            redirectAttributes.addFlashAttribute("toastType", "warning");
            return "redirect:/products/cards"; // Quay lại trang danh sách sản phẩm nếu giỏ hàng trống
        }
        model.addAttribute("cart", cart);
        return "/views/checkout"; // Đảm bảo file `checkout.html` tồn tại
    }

    // Xử lý thanh toán
    @PostMapping("/checkout")
    public String checkout(@RequestParam String name,
                           @RequestParam String address,
                           @RequestParam String payment,
                           HttpSession session,
                           RedirectAttributes redirectAttributes) {
        List<Product> cart = (List<Product>) session.getAttribute("cart");
        if (cart == null || cart.isEmpty()) {
            redirectAttributes.addFlashAttribute("toastMessage", "Giỏ hàng của bạn đang trống, không thể thanh toán!");
            redirectAttributes.addFlashAttribute("toastType", "error");
            return "redirect:/products/cards";
        }

        try {
            Order order = orderService.createOrder(name, address, payment, cart);
            session.removeAttribute("cart"); // Xóa giỏ hàng sau khi thanh toán thành công
            redirectAttributes.addFlashAttribute("toastMessage", "Đơn hàng của bạn đã được đặt thành công!");
            redirectAttributes.addFlashAttribute("toastType", "success");
            return "redirect:/products/order-history";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("toastMessage", "Có lỗi xảy ra khi xử lý thanh toán!");
            redirectAttributes.addFlashAttribute("toastType", "error");
            return "redirect:/products/checkout";
        }
    }
    // Hiển thị lịch sử đơn hàng
    @GetMapping("/order-history")
    public String orderHistory(Model model) {
        List<Order> orders = orderService.getOrders();
        model.addAttribute("orders", orders);
        return "/views/order_history"; // Đảm bảo file `order_history.html` tồn tại
    }

}

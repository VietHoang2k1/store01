package com.example.store.controller;

import com.example.store.model.Order;
import com.example.store.model.Product;
import com.example.store.service.OrderService;
import com.example.store.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductService productService;

    @GetMapping
    public String getOrders(Model model) {
        List<Order> orders = orderService.getAllOrders();
        model.addAttribute("orders", orders);
        return "admin/order_list";
    }

    @PostMapping("/place")
    public String placeOrder(@RequestParam("productId") Long productId,
                             @RequestParam("quantity") int quantity) {
        Product product = productService.getProductById(productId);

        if (product != null && quantity > 0) {
            Order order = new Order();
            // Thêm sản phẩm vào danh sách sản phẩm của đơn hàng
            order.setProducts(List.of(product)); // Thêm sản phẩm vào danh sách
            order.setQuantity(quantity); // Gán số lượng
            order.setStatus("Processing"); // Trạng thái đơn hàng
            orderService.placeOrder(order); // Lưu đơn hàng
        }

        return "redirect:/products/list"; // Chuyển hướng về danh sách sản phẩm
    }

    @GetMapping("/{id}")
    public String viewOrderDetails(@PathVariable Long id, Model model) {
        Order order = orderService.getOrderById(id);
        if (order != null) {
            model.addAttribute("order", order);
            return "order_details";
        } else {
            return "redirect:/orders";
        }
    }

    @PostMapping("/update-status/{id}")
    public String updateOrderStatus(@PathVariable Long id, @RequestParam String status, RedirectAttributes redirectAttributes) {
        Order order = orderService.getOrderById(id);
        if (order != null) {
            order.setStatus(status);
            orderService.saveOrder(order);
            redirectAttributes.addFlashAttribute("success", "Cập nhật trạng thái đơn hàng thành công!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy đơn hàng!");
        }
        return "redirect:/orders";
    }

    @GetMapping("/history")
    public String getOrderHistory(Model model) {
        List<Order> orders = orderService.getAllOrders();
        model.addAttribute("orders", orders);
        return "views/order_history";
    }
}

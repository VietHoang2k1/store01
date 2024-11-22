package com.example.store.service;

import com.example.store.model.Order;
import com.example.store.model.Product;

import java.util.List;
import java.util.Map;

public interface OrderService {

    // Tính doanh thu
    double calculateDailyRevenue();

    double calculateMonthlyRevenue();

    double calculateYearlyRevenue();

    // Thống kê trạng thái đơn hàng
    Map<String, Long> getOrderStatusStatistics();

    // Tạo đơn hàng
    Order createOrder(String name, String address, String payment, List<Product> products);

    // Thêm đơn hàng
    Order placeOrder(Order order);

    // Lấy danh sách đơn hàng
    List<Order> getAllOrders();

    List<Order> getOrders();

    // Lấy chi tiết đơn hàng
    Order getOrderById(Long id);

    // Lấy thông báo trạng thái
    String getSuccessMessage();

    String getErrorMessage();

    Order saveOrder(Order order);

    long getOrderCountByStatus(String status);

    // Cập nhật trạng thái đơn hàng
    Order updateOrderStatus(Long id, String status);
}

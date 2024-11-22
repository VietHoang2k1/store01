package com.example.store.service.impl;

import com.example.store.model.Order;
import com.example.store.model.Product;
import com.example.store.repository.OrderRepository;
import com.example.store.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {

    private String successMessage;
    private String errorMessage;

    @Autowired
    private OrderRepository orderRepository;

    // Tạo đơn hàng
    @Override
    public Order createOrder(String name, String address, String payment, List<Product> products) {
        try {
            // Kiểm tra danh sách sản phẩm không được null hoặc rỗng
            if (products == null || products.isEmpty()) {
                throw new RuntimeException("Danh sách sản phẩm không hợp lệ!");
            }

            Order order = new Order();
            order.setCustomerName(name);
            order.setAddress(address);
            order.setPaymentMethod(payment);
            order.setDate(new Date());

            // Loại bỏ sản phẩm trùng lặp và gán danh sách sản phẩm vào đơn hàng
            Set<Product> uniqueProducts = new HashSet<>(products);
            order.setProducts(new ArrayList<>(uniqueProducts));

            // Tính tổng giá trị đơn hàng
            int total = uniqueProducts.stream()
                    .mapToInt(product -> product.getNewPrice() != null ? product.getNewPrice() : 0)
                    .sum();
            order.setTotal(total);

            // Lưu đơn hàng
            Order savedOrder = orderRepository.save(order);
            successMessage = "Đơn hàng đã được tạo thành công!";
            return savedOrder;

        } catch (Exception e) {
            errorMessage = "Lỗi khi tạo đơn hàng: " + e.getMessage();
            throw new RuntimeException(errorMessage);
        }
    }

    @Override
    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public long getOrderCountByStatus(String status) {
        return orderRepository.countByStatus(status);
    }

    @Override
    public String getSuccessMessage() {
        return successMessage;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public List<Order> getOrders() {
        return orderRepository.findAll();
    }

    @Override
    public double calculateDailyRevenue() {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);

        Double dailyRevenue = orderRepository.calculateRevenueByDate(startOfDay, endOfDay);
        return dailyRevenue != null ? dailyRevenue : 0.0;
    }

    @Override
    public double calculateMonthlyRevenue() {
        LocalDateTime startOfMonth = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay();
        LocalDateTime endOfMonth = startOfMonth.plusMonths(1);

        Double monthlyRevenue = orderRepository.calculateRevenueByMonth(startOfMonth, endOfMonth);
        return monthlyRevenue != null ? monthlyRevenue : 0.0;
    }

    @Override
    public double calculateYearlyRevenue() {
        LocalDateTime startOfYear = LocalDate.now().with(TemporalAdjusters.firstDayOfYear()).atStartOfDay();
        LocalDateTime endOfYear = startOfYear.plusYears(1);

        Double yearlyRevenue = orderRepository.calculateRevenueByYear(startOfYear, endOfYear);
        return yearlyRevenue != null ? yearlyRevenue : 0.0;
    }

    @Override
    public Map<String, Long> getOrderStatusStatistics() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("completed", orderRepository.countByStatus("Completed"));
        stats.put("processing", orderRepository.countByStatus("Processing"));
        stats.put("canceled", orderRepository.countByStatus("Canceled"));
        return stats;
    }

    @Override
    public Order placeOrder(Order order) {
        order.setDate(new Date()); // Lưu ngày đặt hàng hiện tại
        order.setStatus("Processing"); // Trạng thái mặc định
        return orderRepository.save(order);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order getOrderById(Long id) {
        Optional<Order> order = orderRepository.findById(id);
        return order.orElse(null); // Trả về null nếu không tìm thấy
    }

    // Cập nhật trạng thái đơn hàng
    @Override
    public Order updateOrderStatus(Long id, String status) {
        Optional<Order> optionalOrder = orderRepository.findById(id);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.setStatus(status); // Set the new status
            return orderRepository.save(order); // Save updated order to the database
        } else {
            throw new RuntimeException("Order not found with id: " + id);
        }
    }
}

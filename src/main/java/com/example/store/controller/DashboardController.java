package com.example.store.controller;
import com.example.store.model.Product;
import com.example.store.service.OrderService;
import com.example.store.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

@Controller
public class DashboardController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        // Thống kê doanh thu
        Map<String, Double> revenueStats = Map.of(
                "dailyRevenue", orderService.calculateDailyRevenue(),
                "monthlyRevenue", orderService.calculateMonthlyRevenue(),
                "yearlyRevenue", orderService.calculateYearlyRevenue()
        );

        // Lấy số lượng đơn hàng theo trạng thái
        Map<String, Long> orderStats = Map.of(
                "completed", orderService.getOrderCountByStatus("Completed"),
                "processing", orderService.getOrderCountByStatus("Processing"),
                "canceled", orderService.getOrderCountByStatus("Canceled")
        );

        model.addAttribute("revenueStats", revenueStats);
        model.addAttribute("orderStats", orderStats);

        return "admin/dashboard";
    }
}
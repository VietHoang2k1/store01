package com.example.store.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    // Chuyển hướng từ URL gốc "/" đến "index.html"
    @GetMapping("/")
    public String home() {
        return "index"; // Trả về file index.html
    }
}
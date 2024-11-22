package com.example.store.controller;

import com.example.store.model.Category;
import com.example.store.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public String listCategories(Model model) {
        model.addAttribute("categories", categoryService.findAll());
        return "/admin/category_list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("category", new Category());
        return "/admin/category_form";
    }

    @PostMapping
    public String saveCategory(@ModelAttribute("category") Category category, RedirectAttributes redirectAttributes) {
        try {
            categoryService.save(category);
            redirectAttributes.addFlashAttribute("toastMessage", "Danh mục đã được lưu thành công!");
            redirectAttributes.addFlashAttribute("toastType", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("toastMessage", "Đã xảy ra lỗi khi lưu danh mục!");
            redirectAttributes.addFlashAttribute("toastType", "error");
        }
        return "redirect:/categories";
    }

    @GetMapping("/view/{id}")
    public String viewCategory(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        Category category = categoryService.findById(id);
        if (category == null) {
            redirectAttributes.addFlashAttribute("toastMessage", "Không tìm thấy danh mục!");
            redirectAttributes.addFlashAttribute("toastType", "error");
            return "redirect:/categories";
        }
        model.addAttribute("category", category);
        return "/views/category_view";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        Category category = categoryService.findById(id);
        if (category == null) {
            redirectAttributes.addFlashAttribute("toastMessage", "Không tìm thấy danh mục để sửa!");
            redirectAttributes.addFlashAttribute("toastType", "error");
            return "redirect:/categories";
        }
        model.addAttribute("category", category);
        return "/admin/category_form";
    }

    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            categoryService.deleteById(id);
            redirectAttributes.addFlashAttribute("toastMessage", "Danh mục đã được xóa thành công!");
            redirectAttributes.addFlashAttribute("toastType", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("toastMessage", "Không thể xóa danh mục. Có lỗi xảy ra!");
            redirectAttributes.addFlashAttribute("toastType", "error");
        }
        return "redirect:/categories";
    }
}

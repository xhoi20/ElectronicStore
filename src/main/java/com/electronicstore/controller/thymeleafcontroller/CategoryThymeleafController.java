package com.electronicstore.controller.thymeleafcontroller;

import com.electronicstore.entity.Category;

import com.electronicstore.service.serviceInterface.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/categories")
public class CategoryThymeleafController {
    @Autowired
    private ICategoryService categoryService;
    @GetMapping
    public String getAllCategory(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            String role = authentication.getAuthorities().stream()
                    .map(grantedAuthority -> grantedAuthority.getAuthority())
                    .findFirst()
                    .map(auth -> auth.replace("ROLE_", ""))
                    .orElse("");

            model.addAttribute("email", email);
            model.addAttribute("role", role);
            Iterable<Category> categories = categoryService.getAllCategories();
            model.addAttribute("categories", categories);
        }
        return "categories-list";
    }
    @GetMapping("/add")
    public String getAddCategory(Model model) {
        model.addAttribute("category", new Category());
        return "categories-form";
    }
    @PostMapping("/save")
    public String saveCategory(@ModelAttribute("category") Category category) {
        Map<String, String> categoryData = new HashMap<>();
        categoryData.put("emmrikategorise", category.getEmmrikategorise());
        categoryData.put("pershkrimi", category.getPershkrimi());
        categoryService.addCategory(categoryData);
        return "redirect:/categories";
    }
    @GetMapping("/edit/{id}")
    public String getEditCategory(Model model, @PathVariable Long id) {
        Category categories = categoryService.getCategoryById(id)
                .orElseThrow(() -> new RuntimeException("Furnitori nuk u gjet nuk u gjet"));
        model.addAttribute("category", categories);

        return "edit-categories";
    }
    @PostMapping("/edit/{id}")
    public String updateCategory(@PathVariable Long id, @ModelAttribute("category") Category category) {
        try {
            Map<String, String> categoryData = new HashMap<>();
            categoryData.put("emmrikategorise", category.getEmmrikategorise());
            categoryData.put("pershkrimi", category.getPershkrimi());
            categoryService.updateCategory(id,categoryData);
            return "redirect:/categories";
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
            return "error";
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
            return "error";
        }
    }
    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable("id") Long id) {
        categoryService.deleteCategory(id);
        return "redirect:/categories";
    }
    }


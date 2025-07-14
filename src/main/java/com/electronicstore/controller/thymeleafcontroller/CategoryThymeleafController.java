package com.electronicstore.controller.thymeleafcontroller;

import com.electronicstore.entity.Category;

import com.electronicstore.service.serviceInterface.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/categories")
public class CategoryThymeleafController {
    @Autowired
    private ICategoryService categoryService;
    @GetMapping
    public String getAllCategory(Model model) {
        Iterable<Category>categories=categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        return "categories-list";
    }
    @GetMapping("/add")
    public String getAddCategory(Model model) {
        model.addAttribute("category", new Category());
        return "categories-form";
    }
    @PostMapping("/save")
    public String saveCategory(@ModelAttribute("category") Category category) {
        categoryService.addCategory(category);
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
            categoryService.updateCategory(category);
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


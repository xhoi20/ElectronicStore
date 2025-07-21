package com.electronicstore.controller;

import com.electronicstore.entity.Category;
import com.electronicstore.service.serviceInterface.ICategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/categories")
public class CategoryController extends BaseController{

    private final ICategoryService categoryService;

    public CategoryController(ICategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<Iterable<Category>> getAllCategories() {
        return new ResponseEntity<>(categoryService.getAllCategories(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> addCategory(@RequestBody Map<String, String> categoryData) {
        try {
            checkManagerRole();
            return categoryService.addCategory(categoryData);

        } catch (Exception e) {
            return handleException(e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteCategory(@PathVariable Long id) {
        try {
            checkManagerRole();
            categoryService.deleteCategory(id);
            return createSuccessResponse(null, "Category deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return handleException(e);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateCategory(@PathVariable Long id, @RequestBody Map<String, String> categoryData) {
        try {
            checkManagerRole();
            return  categoryService.updateCategory(id,categoryData);

        } catch (Exception e) {
            return handleException(e);
        }
    }
    }

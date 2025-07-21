package com.electronicstore.service.serviceInterface;


import com.electronicstore.entity.Category;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ICategoryService {
    Iterable<Category> getAllCategories();
    public ResponseEntity<Map<String, Object>> addCategory(Map<String, String> categoryData);
    void deleteCategory(Long id);
    public ResponseEntity<Map<String, Object>> updateCategory( Long id,  Map<String, String> categoryData);
Optional<Category> getCategoryById(Long id);

}


package com.electronicstore.service.serviceInterface;


import com.electronicstore.entity.Category;

import java.util.List;
import java.util.Optional;

public interface ICategoryService {
    Iterable<Category> getAllCategories();
    Category addCategory(Category category);
    void deleteCategory(Long id);
    Category updateCategory(Category category);
Optional<Category> getCategoryById(Long id);

}


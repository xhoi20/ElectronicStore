package com.electronicstore.service;

import com.electronicstore.entity.Category;
import com.electronicstore.repository.CategoryRepository;
import com.electronicstore.service.serviceInterface.ICategoryService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
@Service
public class CategoryService  extends BaseService implements ICategoryService {
    private final CategoryRepository categoryRepository;
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }
    @Transactional
    public Iterable<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Transactional
    public ResponseEntity<Map<String, Object>> addCategory(Map<String, String> categoryData) {
        getAuthenticatedUser();
        try {
            String emmrikategorise = categoryData.get("emmrikategorise");
            if (emmrikategorise == null || emmrikategorise.trim().isEmpty()) {
                return createErrorResponse("Category name cannot be null or empty", HttpStatus.BAD_REQUEST);
            }

            Category category = new Category();
            category.setEmmrikategorise(emmrikategorise);
            Category createdCategory = categoryRepository.save(category);
            return createSuccessResponse(createdCategory, "Category added successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            return handleException(e);
        }
    }

    @Transactional
    public void deleteCategory(Long id){
        getAuthenticatedUser();
        if(!categoryRepository.existsById(id)){
            throw new IllegalArgumentException("Category does not exist");
        }
        categoryRepository.deleteById(id);

    }
    @Transactional
    public ResponseEntity<Map<String, Object>> updateCategory( Long id,  Map<String, String> categoryData) {
        try {

                  String emmrikategorise = categoryData.get("emmrikategorise");
            if (emmrikategorise == null || emmrikategorise.trim().isEmpty()) {
                return createErrorResponse("Category name cannot be null or empty", HttpStatus.BAD_REQUEST);
            }

            Category category = new Category();
            category.setId(id);
            category.setEmmrikategorise(emmrikategorise);
            Category updatedCategory = categoryRepository.save(category);
            return createSuccessResponse(updatedCategory, "Category updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            return handleException(e);
        }}
    @Transactional
    public Optional<Category> getCategoryById(Long id){
        return categoryRepository.findById(id);
    }
}

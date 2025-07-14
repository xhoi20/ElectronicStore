package com.electronicstore.service;

import com.electronicstore.entity.Category;
import com.electronicstore.repository.CategoryRepository;
import com.electronicstore.service.serviceInterface.ICategoryService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class CategoryService implements ICategoryService {
    private final CategoryRepository categoryRepository;
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;


    }
    @Transactional

    public Iterable<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
    @Transactional
    public Category addCategory(Category category){
        if(category.getEmmrikategorise()==null||category.getEmmrikategorise().trim().isEmpty()){
            throw new IllegalArgumentException("Category name cannot be null or empty");

        }

        return categoryRepository.save(category);
    }
    @Transactional
    public void deleteCategory(Long id){
        if(!categoryRepository.existsById(id)){
            throw new IllegalArgumentException("Category does not exist");
        }
        categoryRepository.deleteById(id);

    }
    @Transactional
    public Category updateCategory(Category category){
        Optional<Category> optionalCategory = categoryRepository.findById(category.getId());
        if(!optionalCategory.isPresent()){
            throw new IllegalArgumentException("Category does not exist");
        }
        if(category.getEmmrikategorise()==null||category.getEmmrikategorise().trim().isEmpty()){
            throw new IllegalArgumentException("Category name cannot be null or empty");

        }
        Category existingCategory=optionalCategory.get();
        existingCategory.setEmmrikategorise(category.getEmmrikategorise());
        return categoryRepository.save(existingCategory);


    }
    @Transactional
    public Optional<Category> getCategoryById(Long id){
        return categoryRepository.findById(id);
    }
}

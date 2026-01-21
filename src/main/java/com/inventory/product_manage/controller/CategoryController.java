package com.inventory.product_manage.controller;


import com.inventory.product_manage.model.Category;
import com.inventory.product_manage.repository.CategoryRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // ➕ Add Category
    @PostMapping
    public String addCategory(@RequestBody Category category) {
        categoryRepository.save(category);
        return "Category Added Successfully";
    }
    @PutMapping("/{id}")
    public String updateCategory(@PathVariable Long id, @RequestBody Category category) {
        Category c = categoryRepository.findById(id).orElseThrow();
        c.setCategoryName(category.getCategoryName());
        c.setDescription(category.getDescription());
        categoryRepository.save(c);
        return "Category Updated";
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        try {
            categoryRepository.deleteById(id);
            return ResponseEntity.ok("Category deleted successfully");
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body("Cannot delete category. Products are using this category.");
        }
    }


    // 📋 Get All Categories
    @GetMapping
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
}

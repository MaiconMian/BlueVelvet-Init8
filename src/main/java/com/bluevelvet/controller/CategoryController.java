package com.bluevelvet.controller;

import com.bluevelvet.DTO.CategoryDTO;
import com.bluevelvet.DTO.ProductDTO;
import com.bluevelvet.model.ApiResponse;
import com.bluevelvet.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.bluevelvet.model.*;

import java.util.List;
import java.util.Optional;

@RestController
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/categories")
    @PreAuthorize("permitAll()")
    public ResponseEntity<ApiResponse<Object>> getAllCategories(){
        List<Category> categories = categoryService.getAllCategories();
        if (categories.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>("error", "No categories registered"));
        }
        return ResponseEntity.ok(new ApiResponse<>("success", categories));
    }

    @GetMapping("/categories/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<ApiResponse<Object>> getCategoryById(@PathVariable int id) {
        Optional<Category> category = categoryService.getCategoryById(id);
        if (!category.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>("error", "Category not found"));
        }
        return ResponseEntity.ok(new ApiResponse<>("success", category.get()));
    }

    @DeleteMapping("/categories/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    public ResponseEntity<ApiResponse<Object>> deleteCategoryById(@PathVariable int id) {
        boolean deleted = categoryService.deleteCategory(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>("error", "Category not found"));
        }
    }

    @PostMapping("/categories")
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    public ResponseEntity<ApiResponse<String>> addCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
        Category newcategory = categoryService.saveCategory(categoryDTO);
        return ResponseEntity.ok(new ApiResponse<>("success", "Product with ID " +
                newcategory.getId() + " added successfully"));
    }

}

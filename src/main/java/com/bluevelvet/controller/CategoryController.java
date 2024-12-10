package com.bluevelvet.controller;

import com.bluevelvet.DTO.CategoryDTO;
import com.bluevelvet.DTO.ProductDTO;
import com.bluevelvet.model.ApiResponse;
import com.bluevelvet.repository.BrandRepository;
import com.bluevelvet.repository.CategoryRepository;
import com.bluevelvet.repository.ProductRepository;
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
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private ProductRepository productRepository;




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
    @PreAuthorize("hasAuthority('PERMISSION_CATEGORY_DELETE')")
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
    @PreAuthorize("hasAuthority('PERMISSION_CATEGORY_CREATE')")
    public ResponseEntity<ApiResponse<Object>> addCategory(@Valid @RequestBody CategoryDTO categoryDTO) {

        if (categoryRepository.findByCategoryName(categoryDTO.getCategoryName()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>("error", "Category name already exists"));
        }

        Category newCategory = new Category();
        newCategory.setCategoryName(categoryDTO.getCategoryName());
        newCategory.setImage(categoryDTO.getImage());

        categoryDTO.getBrands().forEach(brandId -> {
            brandRepository.findById(brandId).ifPresentOrElse(brand -> {
                brand.getCategory().add(newCategory);
                newCategory.getBrands().add(brand);
            }, () -> {
                throw new IllegalArgumentException("Brand with ID " + brandId + " not found");
            });
        });

        categoryDTO.getProducts().forEach(productId -> {
            productRepository.findById(productId).ifPresentOrElse(product -> {
                product.getCategories().add(newCategory);
                newCategory.getProducts().add(product);
            }, () -> {
                throw new IllegalArgumentException("Product with ID " + productId + " not found");
            });
        });

        // Salvar nova categoria
        categoryRepository.save(newCategory);

        return ResponseEntity.ok(new ApiResponse<>("success", "Category created successfully"));
    }



    @PutMapping("/categories/{id}")
    @PreAuthorize("hasAuthority('PERMISSION_CATEGORY_EDIT')")
    public ResponseEntity<ApiResponse<Object>> updateCategory(
            @PathVariable int id,
            @Valid @RequestBody CategoryDTO categoryDTO) {

        // Verifica se a categoria existe
        Optional<Category> existingCategory = categoryService.getCategoryById(id);
        if (!existingCategory.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>("error", "Category not found"));
        }

        Category newCategory = existingCategory.get();
        newCategory.setImage(categoryDTO.getImage());
        newCategory.setCategoryName(categoryDTO.getCategoryName());


        // Atualiza a categoria usando o método saveCategory no CategoryService
        try {
            Category updatedCategory = categoryService.saveCategory(newCategory);
            return ResponseEntity.ok(new ApiResponse<>("success", "Category updated successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("error", "An error occurred while updating the category"));
        }
    }




}

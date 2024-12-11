package com.bluevelvet.controller;

import com.bluevelvet.DTO.BrandDTO;
import com.bluevelvet.DTO.CategoryDTO;
import com.bluevelvet.model.ApiResponse;
import com.bluevelvet.repository.BrandRepository;
import com.bluevelvet.repository.CategoryRepository;
import com.bluevelvet.repository.ProductRepository;
import com.bluevelvet.service.BrandService;
import com.bluevelvet.service.ProductService;
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
public class BrandController {

    @Autowired
    private BrandService brandService;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductService productService;

    @GetMapping("/brands")
    @PreAuthorize("permitAll()")
    public ResponseEntity<ApiResponse<Object>> getAllBrands(){
        List<Brand> brands = brandService.getAllBrands();
        if (brands.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>("error", "No brands registered"));
        }
        return ResponseEntity.ok(new ApiResponse<>("success", brands));
    }

    @GetMapping("/brands/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<ApiResponse<Object>> getBrandById(@PathVariable int id) {
        Optional<Brand> brand = brandService.getBrandById(id);
        if (!brand.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>("error", "Brand not found"));
        }
        return ResponseEntity.ok(new ApiResponse<>("success", brand.get()));
    }

    @DeleteMapping("/brands/{id}")
    @PreAuthorize("hasAuthority('PERMISSION_BRAND_DELETE')")
    public ResponseEntity<ApiResponse<Object>> deleteBrandById(@PathVariable int id) {
        List<Product> allProducts = productRepository.findAll();
        Brand b = brandService.getBrandById(id).get();
        for (Product p : allProducts) {
            if (p.getBrand() == b) {
                productService.deleteProduct(p.getId());
            }
        }
        boolean deleted = brandService.deleteBrand(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>("error", "Brand not found"));
        }
    }

    @PostMapping("/brands")
    @PreAuthorize("hasAuthority('PERMISSION_BRAND_CREATE')")
    public ResponseEntity<ApiResponse<String>> addBrand(@Valid @RequestBody BrandDTO brandDTO) {
        Brand newBrand = brandService.saveBrand(brandDTO);
        return ResponseEntity.ok(new ApiResponse<>("success", "Brand with ID " +
                newBrand.getId() + " added successfully"));
    }

    @PutMapping("/brands/{id}")
    @PreAuthorize("hasAuthority('PERMISSION_BRAND_EDIT')")
    public ResponseEntity<ApiResponse<String>> updateBrand(@PathVariable int id, @Valid @RequestBody BrandDTO brandDTO) {
        Optional<Brand> brandOptional = brandService.getBrandById(id);
        if (!brandOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>("error", "Brand not found"));
        }

        Brand existingBrand = brandOptional.get();
        existingBrand.setBrandName(brandDTO.getBrandName());
        existingBrand.setImage(brandDTO.getImage());

        // Update categories
        if (brandDTO.getCategory() != null) {
            existingBrand.getCategory().clear();
            brandDTO.getCategory().forEach(categoryId -> {
                Category category = categoryRepository.findById(categoryId)
                        .orElseThrow(() -> new IllegalArgumentException("Category not found for ID: " + categoryId));
                existingBrand.getCategory().add(category);
                if (!category.getBrands().contains(existingBrand)) {
                    category.getBrands().add(existingBrand);
                }
            });
        }

        // Update products
        if (brandDTO.getProducts() != null) {
            existingBrand.getProducts().clear();
            brandDTO.getProducts().forEach(productId -> {
                Product product = productRepository.findById(productId)
                        .orElseThrow(() -> new IllegalArgumentException("Product not found for ID: " + productId));
                existingBrand.getProducts().add(product);
                product.setBrand(existingBrand);
            });
        }

        brandService.updateBrand(id, existingBrand);

        return ResponseEntity.ok(new ApiResponse<>("success", "Brand updated successfully"));
    }


}

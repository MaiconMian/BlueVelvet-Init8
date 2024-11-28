package com.bluevelvet.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

import com.bluevelvet.service.*;
import com.bluevelvet.model.*;
import com.bluevelvet.DTO.*;

@RestController
public class  ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/products")
    @PreAuthorize("permitAll()")
    public ResponseEntity<ApiResponse<Object>> getAllProducts(){
        List<Product> products = productService.getAllProducts();
        if (products.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>("error", "No products registered"));
        }
        return ResponseEntity.ok(new ApiResponse<>("success", products));
    }

    @GetMapping("/products/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<ApiResponse<Object>> getProductById(@PathVariable int id) {
        Optional<Product> product = productService.getProductById(id);
        if (!product.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>("error", "Product not found"));
        }
        return ResponseEntity.ok(new ApiResponse<>("success", product.get()));
    }

    @DeleteMapping("/products/{id}")
    @PreAuthorize("hasAuthority('PERMISSION_DELETE_PRODUCT')")
    public ResponseEntity<ApiResponse<Object>> deleteProductById(@PathVariable int id) {
        boolean deleted = productService.deleteProduct(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>("error", "Product not found"));
        }
    }

    @PostMapping("/products")
    @PreAuthorize("hasAuthority('PERMISSION_EDIT_CREATE_PRODUCT')")
    public ResponseEntity<ApiResponse<String>> addProduct(@Valid @RequestBody ProductDTO productDTO) {
        Product createdProduct = productService.saveProductWithDetails(productDTO);
        return ResponseEntity.ok(new ApiResponse<>("success", "Product with ID " +
                createdProduct.getId() + " added successfully"));
    }

}

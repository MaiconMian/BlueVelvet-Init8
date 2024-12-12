package com.bluevelvet.controller;

import com.bluevelvet.exception.BrandNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.bluevelvet.service.*;
import com.bluevelvet.model.*;
import com.bluevelvet.DTO.*;

@RestController
public class  ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductDetailsService productDetailsService;

    @Autowired
    private ProductPhotosService productPhotosService;

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
    @PreAuthorize("hasAuthority('PERMISSION_PRODUCT_DELETE')")
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
    @PreAuthorize("hasAuthority('PERMISSION_PRODUCT_CREATE')")
    public ResponseEntity<ApiResponse<String>> addProduct(@Valid @RequestBody ProductDTO productDTO) {
        Product createdProduct = productService.saveProductWithDetails(productDTO);
        return ResponseEntity.ok(new ApiResponse<>("success", "Product with ID " +
                createdProduct.getId() + " added successfully"));
    }

    @PutMapping("/products/{id}")
    @PreAuthorize("hasAuthority('PERMISSION_PRODUCT_EDIT')")
    public ResponseEntity<ApiResponse<String>> updateProduct(@PathVariable int id, @Valid @RequestBody ProductDTO productDTO) {

        Optional<Product> existingProduct = productService.getProductById(id);
        if (existingProduct.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>("error", "Product not found"));
        }

        Product product = existingProduct.get();

        product.setName(productDTO.getName());
        product.setImage(productDTO.getImage());
        product.setShortDescription(productDTO.getShortDescription());
        product.setLongDescription(productDTO.getLongDescription());
        product.setPrice(productDTO.getPrice());
        product.setDiscount(productDTO.getDiscount());
        product.setStatus(productDTO.getStatus());
        product.setHasStock(productDTO.getHasStock());
        product.setWidth(productDTO.getWidth());
        product.setLength(productDTO.getLength());
        product.setHeight(productDTO.getHeight());
        product.setCost(productDTO.getCost());
        product.setUpdateTime(LocalDateTime.now());

        brandService.getBrandById(productDTO.getBrand())
                .ifPresentOrElse(
                        brand -> {
                            product.setBrand(brand);
                            brand.getProducts().add(product);
                            brandService.saveBrand(brand);
                        },
                        () -> {
                            throw new BrandNotFoundException("Brand with ID " + productDTO.getBrand() + " not found!");
                        }
                );

        if(productDTO.getCategories() != null) {
            productService.clearProductCategories(product);
            productDTO.getCategories().forEach(categoryId -> {
                categoryService.getCategoryById(categoryId).ifPresent(category -> {
                    product.getCategories().add(category);
                    category.getProducts().add(product);
                    categoryService.saveCategory(category);
                });
            });
        } else {
            productService.clearProductCategories(product);
        }

        if(productDTO.getDetails() != null) {
            productService.clearProductDetails(product);
            productDTO.getDetails().forEach(detailDTO -> {
                ProductDetails productDetails = new ProductDetails();
                productDetails.setDetailName(detailDTO.getDetailName());
                productDetails.setDetailValue(detailDTO.getDetailValue());
                product.getDetails().add(productDetails);
                productDetails.setProduct(product);
            });
        } else {
            productService.clearProductDetails(product);
        }

        if(productDTO.getPhotos() != null) {
            productService.clearProductPhotos(product);
            productDTO.getPhotos().forEach(photoDTO -> {
                ProductPhotos productPhoto = new ProductPhotos();
                productPhoto.setImage(photoDTO.getImage());
                product.getPhotos().add(productPhoto);
                productPhoto.setProduct(product);
            });
        } else {
            productService.clearProductPhotos(product);
        }

        productService.saveProduct(product);

        product.getDetails().forEach(productDetailsService::saveProductDetails);
        product.getPhotos().forEach(productPhotosService::saveProductPhoto);

        return ResponseEntity.ok(new ApiResponse<>("success", "Product with ID " + id + " updated successfully"));
    }
}

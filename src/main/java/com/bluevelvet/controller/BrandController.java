package com.bluevelvet.controller;

import com.bluevelvet.DTO.BrandDTO;
import com.bluevelvet.DTO.CategoryDTO;
import com.bluevelvet.model.ApiResponse;
import com.bluevelvet.service.BrandService;
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
    @PreAuthorize("hasAuthority('PERMISSION_DELETE_BRAND')")
    public ResponseEntity<ApiResponse<Object>> deleteBrandById(@PathVariable int id) {
        boolean deleted = brandService.deleteBrand(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>("error", "Brand not found"));
        }
    }

    @PostMapping("/brands")
    @PreAuthorize("hasAuthority('PERMISSION_DELETE_BRAND')")
    public ResponseEntity<ApiResponse<String>> addBrand(@Valid @RequestBody BrandDTO brandDTO) {
        Brand newBrand = brandService.saveBrand(brandDTO);
        return ResponseEntity.ok(new ApiResponse<>("success", "Brand with ID " +
                newBrand.getId() + " added successfully"));
    }

}

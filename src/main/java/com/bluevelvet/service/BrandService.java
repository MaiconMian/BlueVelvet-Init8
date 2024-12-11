package com.bluevelvet.service;

import com.bluevelvet.DTO.BrandDTO;
import com.bluevelvet.model.Brand;
import com.bluevelvet.model.Category;
import com.bluevelvet.model.Product;
import com.bluevelvet.repository.BrandRepository;
import com.bluevelvet.repository.CategoryRepository;
import com.bluevelvet.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class BrandService {

    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductRepository productRepository;

    public Brand saveBrand(Brand brand) {
        return brandRepository.save(brand);
    }

    public Brand updateBrand(int id, Brand brand) {
        if (brandRepository.existsById(id)) {
            brand.setId(id);
            return brandRepository.save(brand);
        }
        return null;
    }

    public boolean deleteBrand(int id) {
        if (brandRepository.existsById(id)) {
            brandRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Brand> getAllBrands() {
        return brandRepository.findAll();
    }

    public Optional<Brand> getBrandById(int id) {
        return brandRepository.findById(id);
    }

    public Brand saveBrand (BrandDTO brandDTO){
        Brand newBrand = new Brand();

        newBrand.setBrandName(brandDTO.getBrandName());
        this.saveBrand(newBrand);

        if (brandDTO.getCategory() != null) {
            brandDTO.getCategory().forEach(categoryID -> {
                Category category = categoryRepository.findById(categoryID)
                        .orElseThrow(() -> new IllegalArgumentException("Category not fold " + categoryID));
                newBrand.getCategory().add(category);
                category.getBrands().add(newBrand);
                categoryRepository.save(category);
            });
        }
        
        newBrand.setImage(brandDTO.getImage());
        this.saveBrand(newBrand);
        return newBrand;
    }

}

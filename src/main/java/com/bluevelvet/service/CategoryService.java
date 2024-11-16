package com.bluevelvet.service;

import com.bluevelvet.model.Brand;
import com.bluevelvet.model.Category;
import com.bluevelvet.model.Product;
import com.bluevelvet.repository.BrandRepository;
import com.bluevelvet.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import com.bluevelvet.DTO.CategoryDTO;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private BrandService brandService;
    @Autowired
    private ProductService productService;

    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }

    public Category updateCategory(int id, Category category) {
        if (categoryRepository.existsById(id)) {
            category.setId(id);
            return categoryRepository.save(category);
        }
        return null;
    }

    public boolean deleteCategory(int id) {
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Optional<Category> getCategoryById(int id) {
        return categoryRepository.findById(id);
    }

    public Category saveCategory (CategoryDTO categoryDTO){

        Category newcategory = new Category();

        newcategory.setImage(categoryDTO.getImage());
        newcategory.setCategoryName(categoryDTO.getCategoryName());

        this.saveCategory(newcategory);

        if(categoryDTO.getCategoryParent() == null){
            newcategory.setCategoryParent(newcategory);
        } else {
            Category parentCategory = this.getCategoryById(categoryDTO.getCategoryParent())
                    .orElseThrow(() -> new IllegalArgumentException("Parent Category not fold"));
            newcategory.setCategoryParent(parentCategory);
        }

        if (categoryDTO.getBrands() != null) {
            categoryDTO.getBrands().forEach(brandId -> {
                Brand brand = brandService.getBrandById(brandId)
                        .orElseThrow(() -> new IllegalArgumentException("Brand not fold " + brandId));
                newcategory.getBrands().add(brand);
                brand.getCategory().add(newcategory);
                brandService.saveBrand(brand);
            });
        }

        if (categoryDTO.getProducts() != null) {
            categoryDTO.getProducts().forEach(productId -> {
                Product product = productService.getProductById(productId)
                        .orElseThrow(() -> new IllegalArgumentException("Product not found for ID: " + productId));
                newcategory.getProducts().add(product);
                product.getCategories().add(newcategory);
                productService.saveProduct(product);
            });
        }

        this.saveCategory(newcategory);
        return newcategory;

    }
}

package com.bluevelvet.service;

import com.bluevelvet.model.Brand;
import com.bluevelvet.model.Category;
import com.bluevelvet.model.Product;
import com.bluevelvet.repository.BrandRepository;
import com.bluevelvet.repository.CategoryRepository;
import com.bluevelvet.repository.ProductRepository;
import jakarta.transaction.Transactional;
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
    private BrandRepository brandRepository;
    @Autowired
    private ProductRepository productRepository;

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

    @Transactional
    public void removeProductFromAllCategories(int productId) {
        List<Category> categories = categoryRepository.findAll();

        for (Category category : categories) {
            category.getProducts().removeIf(product -> product.getId() == productId);
        }
        categoryRepository.saveAll(categories);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Optional<Category> getCategoryById(int id) {
        return categoryRepository.findById(id);
    }

    public Category saveCategory(CategoryDTO categoryDTO) {

        Category newCategory = new Category();
        newCategory.setImage(categoryDTO.getImage());
        newCategory.setCategoryName(categoryDTO.getCategoryName());

        this.saveCategory(newCategory);

        if (categoryDTO.getCategoryParent() == null) {
            newCategory.setCategoryParent(newCategory);
        } else {
            Category parentCategory = this.getCategoryById(categoryDTO.getCategoryParent())
                    .orElseThrow(() -> new IllegalArgumentException("Parent Category not found"));
            newCategory.setCategoryParent(parentCategory);
        }

        if (categoryDTO.getBrands() != null) {
            categoryDTO.getBrands().forEach(brandId -> {
                Brand brand = brandRepository.findById(brandId)
                        .orElseThrow(() -> new IllegalArgumentException("Brand not found: " + brandId));
                newCategory.getBrands().add(brand);
                brand.getCategory().add(newCategory);
                brandRepository.save(brand);
            });
        }

        return this.saveCategory(newCategory);
    }
}

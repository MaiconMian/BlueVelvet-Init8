package com.bluevelvet.mapper;

import com.bluevelvet.DTO.ProductDTO;
import com.bluevelvet.exception.BrandNotFoundException;
import com.bluevelvet.model.Product;
import com.bluevelvet.service.BrandService;
import com.bluevelvet.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class ProductMapper {

    @Autowired
    private BrandService brandService;

    @Autowired
    private CategoryService categoryService;

    public Product toProduct(ProductDTO productDTO) {
        Product product = new Product();

        LocalDateTime now = LocalDateTime.now();

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
        product.setCreationTime(now);
        product.setUpdateTime(now);

        return product;
    }
}

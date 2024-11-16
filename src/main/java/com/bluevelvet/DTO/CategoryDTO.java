package com.bluevelvet.DTO;

import com.bluevelvet.model.Brand;
import com.bluevelvet.model.Category;
import com.bluevelvet.model.Product;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Set;

@Getter
@Setter
public class CategoryDTO {

    private Integer categoryParent;

    @NotNull(message = "Category Name is required")
    private String categoryName;

    private byte[] image;

    @NotNull(message = "Brand ID is required")
    private Set<Integer> brands;

    private Set<Integer> products;

}

package com.bluevelvet.DTO;

import com.bluevelvet.model.Category;
import com.bluevelvet.model.Product;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;
import java.util.Set;

@Getter
@Setter
public class BrandDTO {

    @NotNull(message = "Brand name is required")
    private String brandName;

    private byte[] image;


    private Set<Integer> category;
}

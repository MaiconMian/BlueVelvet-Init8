package com.bluevelvet.DTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class ProductDTO {

    @NotNull(message = "Brand ID is required")
    private Integer brand;

    @NotEmpty(message = "Product name is required")
    private String name;

    @NotEmpty(message = "Product image is required")
    private byte[] image;

    @NotEmpty(message = "Product short description is required")
    private String shortDescription;

    private String longDescription;

    @Positive(message = "Product price must be positive")
    @NotNull(message = "Product price is required")
    private Float price;

    @PositiveOrZero(message = "Product discount must be positive or zero")
    @NotNull(message = "Product discount is required")
    private Float discount;

    @NotNull(message = "Product status is required")
    private Boolean status;

    @NotNull(message = "Product stock status is required")
    private Boolean hasStock;

    @Positive(message = "Product width must be positive")
    @NotNull(message = "Product width is required")
    private Float width;

    @Positive(message = "Product length must be positive")
    @NotNull(message = "Product length is required")
    private Float length;

    @Positive(message = "Product height must be positive")
    @NotNull(message = "Product height is required")
    private Float height;

    @Positive(message = "Product cost must be positive")
    @NotNull(message = "Product cost is required")
    private Float cost;

    private List<ProductDetailsDTO> details;

    private List<ProductPhotosDTO> photos;

    private Set<Integer> categories;
}




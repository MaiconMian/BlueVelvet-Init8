package com.bluevelvet.DTO;

import com.bluevelvet.model.Product;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
public class ProductDetailsDTO {

    private String detailName;

    private String detailValue;
}

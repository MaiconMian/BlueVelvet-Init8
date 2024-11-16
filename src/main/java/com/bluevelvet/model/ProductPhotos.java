package com.bluevelvet.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import java.util.*;
import java.io.*;

@Entity
@Getter
@Setter
@Table (name = "bv_products_photos")
public class ProductPhotos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "id_product")
    private Product product;

    @Column(name = "products_ph_contents")
    private byte[] image;
}

package com.bluevelvet.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import java.util.*;
import java.io.*;

@Entity
@Setter
@Getter
@Table(name = "bv_products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_brand")
    private Brand brand;

    @Column(name = "product_name", length = 100)
    private String name;

    @Lob
    @Column(name = "product_main_photo", columnDefinition = "MEDIUMBLOB")
    private byte[] image;

    @Column(name = "product_short_desc", length = 100)
    private String shortDescription;

    @Column(name = "product_long_desc", length = 500)
    private String longDescription;

    @Column(name = "product_price")
    private Float price;

    @Column(name = "product_discount")
    private Float discount;

    @Column(name = "products_status", columnDefinition = "TINYINT")
    private Boolean status;

    @Column(name = "products_has_stock", columnDefinition = "TINYINT")
    private Boolean hasStock;

    @Column(name = "product_width")
    private Float width;

    @Column(name = "product_length")
    private Float length;

    @Column(name = "product_height")
    private Float height;

    @Column(name = "product_cost")
    private Float cost;

    @Column(name = "product_creation_time")
    private LocalDateTime creationTime;

    @Column(name = "product_update_time")
    private LocalDateTime updateTime;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductDetails> details = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductPhotos> photos = new ArrayList<>();

    @ManyToMany(mappedBy = "products")
    private Set<Category> categories = new HashSet<>();

}

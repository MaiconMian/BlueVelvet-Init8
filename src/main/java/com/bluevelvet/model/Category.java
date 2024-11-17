package com.bluevelvet.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import java.util.*;
import java.io.*;

@Getter
@Setter
@Entity
@Table (name="bv_categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "category_parent_id", insertable = false, updatable = false)
    private Category categoryParent;

    @Column(name = "category_name", length=100)
    private String categoryName;

    @Lob
    @Column(name = "category_ph_content", columnDefinition = "MEDIUMBLOB")
    private byte[] image;

    @ManyToMany(mappedBy = "category")
    @JsonIgnore
    private Set<Brand> brands;

    @ManyToMany
    @JsonIgnore
    @JoinTable(
            name = "bv_products_categories",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private Set<Product> products = new HashSet<>();

}

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
@Table (name="bv_brands")
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "brand_name", length=100)
    private String brandName;

    @Column(name = "brand_ph_content")
    private byte[] image;

    @JsonIgnore
    @OneToMany(mappedBy = "brand", cascade = CascadeType.ALL)
    private List<Product> products;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "bv_brands_categories",
            joinColumns = @JoinColumn(name = "brand_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> category = new HashSet<>();

}

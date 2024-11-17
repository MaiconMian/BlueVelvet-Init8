package com.bluevelvet.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;

@Entity
@Setter
@Getter
@Table (name = "bv_roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "role_name", length = 100, unique = true)
    private String name;

    @Column(name = "role_description", length = 255)
    private String description;

    @ManyToMany
    @JoinTable(
            name = "bv_users_roles",
            joinColumns = @JoinColumn(name = "roles_id"),
            inverseJoinColumns = @JoinColumn(name = "users_id")
    )
    private Set<User> users = new HashSet<>();
}



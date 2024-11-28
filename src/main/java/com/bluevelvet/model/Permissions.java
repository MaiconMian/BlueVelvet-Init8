package com.bluevelvet.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Setter
@Getter
@Table(name = "bv_permissions")
public class Permissions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "permission_name", length = 100, unique = true)
    private String name;

    @Column(name = "permission_description", length = 255)
    private String description;

    @ManyToMany
    @JsonIgnore
    @JoinTable(
            name = "bv_roles_permissions",
            joinColumns = @JoinColumn(name = "permissions_id"),
            inverseJoinColumns = @JoinColumn(name = "roles_id")
    )
    private Set<Role> roles = new HashSet<>();
}

package com.bluevelvet.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.*;
import org.hibernate.Hibernate;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.security.Permission;
import java.util.*;

@Entity
@Setter
@Getter
@Table(name = "bv_users")
public class User implements UserDetails{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "username", length = 60, nullable = false)
    private String name;

    @Column(name = "user_last_name", length = 60)
    private String lastName;

    @Column(name = "user_email", length = 128, nullable = false, unique = true)
    private String email;

    @Column(name = "user_password", length = 64, nullable = false)
    @JsonIgnore
    private String password;

    @Column(name = "user_status", columnDefinition = "TINYINT", nullable = false)
    private Boolean status;

    @Lob
    @Column(name = "user_ph_content", columnDefinition = "MEDIUMBLOB")
    private byte[] image;

    @ManyToMany(mappedBy = "users", fetch = FetchType.EAGER)
    private Set<Role> roles = new HashSet<>();

    private static final Logger logger = (Logger) LoggerFactory.getLogger(User.class);

    @Override
    @Transactional
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();

        for (Role role : this.getRoles()) {
            Set<Permissions> permissions = role.getPermissions();
            if (permissions != null) {
                for (Permissions permission : permissions) {
                    logger.debug("Adding permission: {}", permission.getName());
                    authorities.add(new SimpleGrantedAuthority(permission.getName()));
                }
            }
        }

        return authorities;
    }


    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.getStatus();
    }

}

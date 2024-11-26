package com.bluevelvet.controller;

import com.bluevelvet.DTO.AdminRegisterDTO;
import com.bluevelvet.DTO.UserRegisterDTO;
import com.bluevelvet.model.ApiResponse;
import com.bluevelvet.model.Product;
import com.bluevelvet.model.User;
import com.bluevelvet.repository.UserRepository;
import com.bluevelvet.security.SecurityFilter;
import com.bluevelvet.security.TokenService;
import com.bluevelvet.service.RoleService;
import com.bluevelvet.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UsersController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Object>> getAllUsers(){
        List<User> users = userService.getAllUsers();
        if (users.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>("error", "No users registered"));
        }
        return ResponseEntity.ok(new ApiResponse<>("success", users));
    }

    @PostMapping("/admin/register")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity adminRegister(@Valid @RequestBody AdminRegisterDTO adminRegisterDTO) {

        if(this.userRepository.findByEmail(adminRegisterDTO.email()) != null) {
            return ResponseEntity.badRequest().build();
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(adminRegisterDTO.password());

        User newAdminUser = new User();
        newAdminUser.setName(adminRegisterDTO.name());
        newAdminUser.setLastName(adminRegisterDTO.lastName());
        newAdminUser.setEmail(adminRegisterDTO.email());
        newAdminUser.setPassword(encryptedPassword);
        newAdminUser.setStatus(adminRegisterDTO.status());

        this.userRepository.save(newAdminUser);

        adminRegisterDTO.Roles().forEach(roleId -> {
            roleService.getRoleById(roleId).ifPresent(role -> {
                newAdminUser.getRoles().add(role);
                role.getUsers().add(newAdminUser);
                roleService.saveRole(role);
            });
        });

        this.userRepository.save(newAdminUser);

        return ResponseEntity.ok().build();

    }

    @PostMapping("/user/register")
    @PreAuthorize("permitAll()")
    public ResponseEntity userRegister(@Valid @RequestBody UserRegisterDTO userRegisterDTO) {

        if(this.userRepository.findByEmail(userRegisterDTO.email()) != null) {
            return ResponseEntity.badRequest().build();
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(userRegisterDTO.password());

        User newNormalUser = new User();
        newNormalUser.setName(userRegisterDTO.name());
        newNormalUser.setLastName(userRegisterDTO.lastName());
        newNormalUser.setEmail(userRegisterDTO.email());
        newNormalUser.setPassword(encryptedPassword);
        newNormalUser.setStatus(true);

        this.userRepository.save(newNormalUser);

        roleService.getRoleById(3).ifPresent(role -> {
            newNormalUser.getRoles().add(role);
            role.getUsers().add(newNormalUser);
            roleService.saveRole(role);
        });

        return ResponseEntity.ok().build();
    }

}

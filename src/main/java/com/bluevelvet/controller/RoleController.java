package com.bluevelvet.controller;

import com.bluevelvet.DTO.AdminRegisterDTO;
import com.bluevelvet.DTO.RoleDTO;
import com.bluevelvet.model.ApiResponse;
import com.bluevelvet.model.Role;
import com.bluevelvet.model.User;
import com.bluevelvet.repository.PermissionsRepository;
import com.bluevelvet.repository.UserRepository;
import com.bluevelvet.service.RoleService;
import com.bluevelvet.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionsRepository permissionsRepository;

    @GetMapping("/roles")
    @PreAuthorize("hasAuthority('PERMISSION_REVIEW_VIEW')")
    public ResponseEntity<ApiResponse<Object>> getAllRoles(){
        List<Role> roles = roleService.getAllRoles();
        if (roles.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>("error", "No roles registered"));
        }
        return ResponseEntity.ok(new ApiResponse<>("success", roles));
    }

    @PostMapping("/roles")
    @PreAuthorize("hasAuthority('PERMISSION_ROLE_CREATE')")
    public ResponseEntity<ApiResponse<Object>> createRole(@Valid @RequestBody RoleDTO roleDTO) {
        // try {
        Role newRole = new Role();
        newRole.setName(roleDTO.getName());
        newRole.setDescription(roleDTO.getDescription());

        Role savedRole = roleService.saveRole(newRole);

        roleDTO.getPermissions().forEach(permissionId -> {
            permissionsRepository.findById(permissionId).ifPresent(permission -> {
                newRole.getPermissions().add(permission);
                permission.getRoles().add(newRole);
                permissionsRepository.save(permission);
            });
        });

        savedRole = roleService.saveRole(newRole);

        return ResponseEntity.ok(new ApiResponse<>(true, savedRole));
        // } catch (Exception e) {
        //     return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        //             .body(new ApiResponse<>(false, "An error occurred: " + e.getMessage()));
        // }
    }


}

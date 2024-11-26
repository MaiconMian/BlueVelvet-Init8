package com.bluevelvet.controller;

import com.bluevelvet.model.ApiResponse;
import com.bluevelvet.model.Role;
import com.bluevelvet.model.User;
import com.bluevelvet.repository.UserRepository;
import com.bluevelvet.service.RoleService;
import com.bluevelvet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping("/roles")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Object>> getAllRoles(){
        List<Role> roles = roleService.getAllRoles();
        if (roles.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>("error", "No roles registered"));
        }
        return ResponseEntity.ok(new ApiResponse<>("success", roles));
    }

}

package com.bluevelvet.controller;

import com.bluevelvet.model.ApiResponse;
import com.bluevelvet.model.Permissions;
import com.bluevelvet.model.Role;
import com.bluevelvet.service.PermissionsService;
import com.bluevelvet.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PermissionController {

    @Autowired
    private PermissionsService permissionsService;

    @GetMapping("/permissions")
    @PreAuthorize("hasAuthority('PERMISSION_VIEW_USER')")
    public ResponseEntity<ApiResponse<Object>> getAllPermissions(){
        List<Permissions> permissions = permissionsService.findAll();
        if (permissions.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>("error", "No permissions registered"));
        }
        return ResponseEntity.ok(new ApiResponse<>("success", permissions));
    }
}

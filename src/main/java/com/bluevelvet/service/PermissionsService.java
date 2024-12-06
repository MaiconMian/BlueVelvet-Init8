package com.bluevelvet.service;

import com.bluevelvet.model.Category;
import com.bluevelvet.model.Permissions;
import com.bluevelvet.repository.PermissionsRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PermissionsService {

    @Autowired
    private PermissionsRepository permissionsRepository;

    public Permissions save(Permissions permissions) {
        return permissionsRepository.save(permissions);
    }

    public Permissions update(int id, Permissions updatedPermissions) {
        Optional<Permissions> optionalPermission = permissionsRepository.findById(id);
        if (optionalPermission.isPresent()) {
            Permissions permission = optionalPermission.get();
            permission.setName(updatedPermissions.getName());
            permission.setDescription(updatedPermissions.getDescription());
            return permissionsRepository.save(permission);
        }
        throw new RuntimeException("Permission not found");
    }

    @Transactional
    public void removeRoleFromAllPermissions(int roleId) {
        List<Permissions> permissions = permissionsRepository.findAll();

        for (Permissions permission : permissions) {
            permission.getRoles().removeIf(role -> role.getId() == roleId);
        }
        permissionsRepository.saveAll(permissions);
    }

    public List<Permissions> findAll() {
        return permissionsRepository.findAll();
    }

    public Optional<Permissions> findById(int id) {
        return permissionsRepository.findById(id);
    }

    public void deleteById(int id) {
        permissionsRepository.deleteById(id);
    }
}

package com.bluevelvet.service;

import com.bluevelvet.model.Permissions;
import com.bluevelvet.model.Role;
import com.bluevelvet.repository.RoleRepository;
import com.bluevelvet.repository.PermissionsRepository;  // Adiciona o repositório de Permissions
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermissionsRepository permissionsRepository;

    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public Optional<Role> getRoleById(int id) {
        return roleRepository.findById(id);
    }

    public void deleteRole(int id) {
        roleRepository.deleteById(id);
    }

    @Transactional
    public void removeUserFromAllRoles(int userId) {
        List<Role> roles = roleRepository.findAll();

        for (Role role : roles) {
            role.getUsers().removeIf(user -> user.getId() == userId);
        }
        roleRepository.saveAll(roles);
    }

    @Transactional
    public void addPermissionsToRole(int roleId, List<Integer> permissionIds) {
        Optional<Role> optionalRole = roleRepository.findById(roleId);
        if (optionalRole.isPresent()) {
            Role role = optionalRole.get();

            List<Permissions> permissions = permissionsRepository.findAllById(permissionIds);

            role.getPermissions().addAll(permissions);

            roleRepository.save(role);
        }
    }

    @Transactional
    public void removePermissionsFromRole(int roleId, List<Integer> permissionIds) {
        Optional<Role> optionalRole = roleRepository.findById(roleId);
        if (optionalRole.isPresent()) {
            Role role = optionalRole.get();

            List<Permissions> permissions = permissionsRepository.findAllById(permissionIds);

            role.getPermissions().removeAll(permissions);

            roleRepository.save(role);
        }
    }
}

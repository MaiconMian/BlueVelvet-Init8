package com.bluevelvet.service;

import com.bluevelvet.model.Category;
import com.bluevelvet.model.Role;
import com.bluevelvet.repository.RoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

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
}

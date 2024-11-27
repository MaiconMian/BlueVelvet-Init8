package com.bluevelvet.service;

import com.bluevelvet.model.Brand;
import com.bluevelvet.model.Product;
import com.bluevelvet.model.User;
import com.bluevelvet.repository.RoleRepository;
import com.bluevelvet.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleService roleService;

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(int id) {
        return userRepository.findById(id);
    }

    public User updateUser(int id, User user) {
        if (userRepository.existsById(id)) {
            user.setId(id);
            return userRepository.save(user);
        }
        return null;
    }

    public boolean deleteUser(int id) {
        if (userRepository.existsById(id)) {
            User user = userRepository.findById(id).orElseThrow();
            user.getRoles().clear();
            userRepository.save(user);
            roleService.removeUserFromAllRoles(id);
            userRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}

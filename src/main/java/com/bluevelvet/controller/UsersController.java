package com.bluevelvet.controller;

import com.bluevelvet.DTO.AdminRegisterDTO;
import com.bluevelvet.DTO.BrandDTO;
import com.bluevelvet.DTO.ProductDTO;
import com.bluevelvet.DTO.UserRegisterDTO;
import com.bluevelvet.model.ApiResponse;
import com.bluevelvet.model.Brand;
import com.bluevelvet.model.Product;
import com.bluevelvet.model.User;
import com.bluevelvet.repository.UserRepository;
import com.bluevelvet.security.SecurityFilter;
import com.bluevelvet.security.TokenService;
import com.bluevelvet.service.RoleService;
import com.bluevelvet.service.UserService;
import jakarta.validation.Valid;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.bluevelvet.DTO.UserUpdateDTO;

import java.util.Base64;
import java.util.List;
import java.util.Optional;

@RestController
public class UsersController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('PERMISSION_USER_VIEW')")
    public ResponseEntity<ApiResponse<Object>> getAllUsers(){
        List<User> users = userService.getAllUsers();
        if (users.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>("error", "No users registered"));
        }
        return ResponseEntity.ok(new ApiResponse<>("success", users));
    }

    @GetMapping("/users/{id}")
    @PreAuthorize("hasAuthority('PERMISSION_USER_VIEW')")
    public ResponseEntity<ApiResponse<Object>> getUserById(@PathVariable int id) {
        Optional<User> user = userService.getUserById(id);
        if (!user.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>("error", "User not found"));
        }
        return ResponseEntity.ok(new ApiResponse<>("success", user.get()));
    }

    @DeleteMapping("/users/{id}")
    @PreAuthorize("hasAuthority('PERMISSION_USER_DELETE')")
    public ResponseEntity<ApiResponse<Object>> deleteUserById(@RequestHeader(HttpHeaders.COOKIE) String cookieHeader, @PathVariable int id) {

        String token = extractTokenFromCookie(cookieHeader, "jwt");

        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse<>("Token not found", null));
        }

        String email = decodeToken(token);
        User user = (User) userRepository.findByEmail(email);

        if(user.getId() == id){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse<>("You can't delete yourself", null));
        } else {
            boolean deleted = userService.deleteUser(id);
            if (deleted) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>("error", "User not found"));
            }
        }
    }

    private String decodeToken(String token){
        String[] chunks = token.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String payload = new String(decoder.decode(chunks[1]));
        JSONObject json = new JSONObject(payload);
        return json.getString("email");
    }

    private String extractTokenFromCookie(String cookieHeader, String cookieName) {
        if (cookieHeader != null && !cookieHeader.isEmpty()) {
            String[] cookies = cookieHeader.split(";");
            for (String cookie : cookies) {
                cookie = cookie.trim();
                if (cookie.startsWith(cookieName + "=")) {
                    return cookie.substring(cookieName.length() + 1);
                }
            }
        }
        return null;
    }

    @PostMapping("/users")
    @PreAuthorize("hasAuthority('PERMISSION_USER_CREATE')")
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
        newAdminUser.setImage(adminRegisterDTO.image());

        this.userRepository.save(newAdminUser);

        adminRegisterDTO.roles().forEach(roleId -> {
            roleService.getRoleById(roleId).ifPresent(role -> {
                newAdminUser.getRoles().add(role);
                role.getUsers().add(newAdminUser);
                roleService.saveRole(role);
            });
        });

        this.userRepository.save(newAdminUser);

        return ResponseEntity.ok().build();

    }

    @PutMapping("/users/{id}")
    @PreAuthorize("hasAuthority('PERMISSION_USER_EDIT')")
    public ResponseEntity<ApiResponse<Object>> updateUserById(
            @PathVariable int id,
            @Valid @RequestBody UserUpdateDTO userUpdateDTO) {

        // Verifica se o usuário existe
        Optional<User> existingUser = userService.getUserById(id);
        if (!existingUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>("error", "User not found"));
        }

        User user = existingUser.get();

        // Loop através de todos os usuários para verificar se o e-mail está em uso
        List<User> allUsers = userRepository.findAll();
        for (User u : allUsers) {
            if (u.getEmail().equalsIgnoreCase(userUpdateDTO.getEmail()) && u.getId() != user.getId()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse<>("error", "Email is already in use"));
            }
        }

        // Atualiza os campos do usuário com os valores fornecidos
        user.setName(userUpdateDTO.getName());
        user.setLastName(userUpdateDTO.getLastName());
        user.setEmail(userUpdateDTO.getEmail());
        user.setStatus(userUpdateDTO.getStatus());

        if(userUpdateDTO.getImage() != null){
            user.setImage(userUpdateDTO.getImage());
        }

        // Atualiza a senha apenas se for fornecida
        if (userUpdateDTO.getPassword() != null && !userUpdateDTO.getPassword().isEmpty()) {
            String encryptedPassword = new BCryptPasswordEncoder().encode(userUpdateDTO.getPassword());
            user.setPassword(encryptedPassword);
        }

        user.getRoles().forEach(role -> {
            role.getUsers().remove(user);
            roleService.saveRole(role);
        });

        user.getRoles().clear();

        userUpdateDTO.getRoles().forEach(roleId -> {
            roleService.getRoleById(roleId).ifPresent(role -> {
                user.getRoles().add(role);
                role.getUsers().add(user);
                roleService.saveRole(role);
            });
        });


        // Salva o usuário atualizado no banco de dados
        this.userRepository.save(user);

        return ResponseEntity.ok(new ApiResponse<>("success", "User updated successfully"));
    }
}


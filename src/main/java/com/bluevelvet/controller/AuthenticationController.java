package com.bluevelvet.controller;

import com.bluevelvet.DTO.AdminRegisterDTO;
import com.bluevelvet.DTO.LoginDTO;
import com.bluevelvet.DTO.UserRegisterDTO;
import com.bluevelvet.repository.UserRepository;
import com.bluevelvet.security.TokenService;
import com.bluevelvet.service.RoleService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.bluevelvet.model.User;
import com.bluevelvet.security.SecurityFilter;

@RestController
@RequestMapping("auth")
public class AuthenticationController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleService roleService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private SecurityFilter securityHelper;

    @PostMapping("/login")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDTO loginDTO) {
        var userPassword = new UsernamePasswordAuthenticationToken(loginDTO.email(), loginDTO.password());
        try {
            var auth = this.authenticationManager.authenticate(userPassword);
            var token = tokenService.generateToken((User) auth.getPrincipal());

            ResponseCookie cookie = ResponseCookie.from("jwt", token)
                    .httpOnly(false)
                    .secure(true)
                    .path("/")
                    .sameSite("None")
                    .maxAge(60 * 60)
                    .build();

            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body("Login successful");
        } catch (Exception e) {
            return ResponseEntity.status(401).build();
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        ResponseCookie cookie = ResponseCookie.from("jwt", "")
                .httpOnly(false)
                .secure(true)
                .path("/")
                .maxAge(0)
                .build();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body("Logout successful");
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
        newAdminUser.setStatus(true);

        this.userRepository.save(newAdminUser);

        adminRegisterDTO.Roles().forEach(roleId -> {
            roleService.getRoleById(roleId).ifPresent(role -> {
                newAdminUser.getRoles().add(role);
                role.getUsers().add(newAdminUser);
                roleService.saveRole(role);
            });
        });

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

    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(HttpServletRequest request) {

        String token = securityHelper.recoveryToken(request);
        if(token != null) {
            try {
                tokenService.validateToken(token);
                return ResponseEntity.ok("Valid Token");
            } catch (RuntimeException e) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Token");
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Token");
    }
}

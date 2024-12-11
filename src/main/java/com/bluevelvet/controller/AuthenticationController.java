package com.bluevelvet.controller;

import com.bluevelvet.DTO.AdminRegisterDTO;
import com.bluevelvet.DTO.UserInformationsDTO;
import com.bluevelvet.DTO.LoginDTO;
import com.bluevelvet.DTO.UserRegisterDTO;
import com.bluevelvet.model.ApiResponse;
import com.bluevelvet.model.Role;
import com.bluevelvet.repository.UserRepository;
import com.bluevelvet.security.TokenService;
import com.bluevelvet.service.RoleService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import io.jsonwebtoken.Jwts;
import org.json.JSONObject;
import java.security.SignatureException;
import java.util.*;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtParserBuilder;

import static javax.crypto.Cipher.SECRET_KEY;

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

    @Value("${api.security.token.secret}")
    private static String secret = "my-secret-key";

    @GetMapping("/me")
    @PreAuthorize("permitAll()")
    public ResponseEntity<ApiResponse<Object>> getUserDetails(@RequestHeader(HttpHeaders.COOKIE) String cookieHeader){
        try {

            String token = extractTokenFromCookie(cookieHeader, "jwt");

            if (token == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse<>("Token not found", null));
            }

            String email = decodeToken(token);
            User user = (User) userRepository.findByEmail(email);
            List<String> roleNames = new ArrayList<>();

            for (Role role : user.getRoles()) {
                roleNames.add(role.getName());
            }

            UserInformationsDTO userInformatiossDTO = new UserInformationsDTO(user.getName(), user.getLastName(), user.getEmail(), user.getImage(), roleNames);

            return ResponseEntity.ok(new ApiResponse<>("User details fetched successfully", userInformatiossDTO));
        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse<>(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>(e.getMessage(), null));
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
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> logout() {
        ResponseCookie cookie = ResponseCookie.from("jwt", "")
                .httpOnly(false)
                .secure(true)
                .path("/")
                .maxAge(0)
                .build();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body("Logout successful");
    }

    @GetMapping("/validate")
    @PreAuthorize("permitAll()")
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

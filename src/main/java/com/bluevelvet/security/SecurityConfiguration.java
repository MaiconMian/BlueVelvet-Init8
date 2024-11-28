package com.bluevelvet.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

    @Autowired
    SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize

                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/brands/**").permitAll()
//                        .requestMatchers(HttpMethod.PUT, "/brands/**").hasRole("ADMIN")
//                        .requestMatchers(HttpMethod.POST, "/brands").hasRole("ADMIN")
//                        .requestMatchers(HttpMethod.DELETE, "/brands/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/categories/**").permitAll()
//                        .requestMatchers(HttpMethod.PUT, "/categories/**").hasRole("ADMIN")
//                        .requestMatchers(HttpMethod.POST, "/categories").hasRole("ADMIN")
//                        .requestMatchers(HttpMethod.DELETE, "/categories/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/products/**").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/products/**").hasAuthority("PERMISSION_EDIT_CREATE_PRODUCT")
                        .requestMatchers(HttpMethod.POST, "/products").hasAuthority("PERMISSION_EDIT_CREATE_PRODUCT")
                        .requestMatchers(HttpMethod.DELETE, "/products/**").hasAuthority("PERMISSION_DELETE_PRODUCT")

                        .requestMatchers(HttpMethod.GET, "/users/**").hasAuthority("PERMISSION_USER_VIEW")
                        .requestMatchers(HttpMethod.PUT, "/users/**").hasAuthority("PERMISSION_CREATE_EDIT_USER")
                        .requestMatchers(HttpMethod.POST, "/users").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/users/**").hasAuthority("PERMISSION_DELETE_USER")

                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/admins").hasAuthority("PERMISSION_CREATE_EDIT_USER")

                        .requestMatchers(HttpMethod.GET, "/roles/**").hasAuthority("PERMISSION_VIEW_USER")
                        .requestMatchers(HttpMethod.POST, "/roles").hasAuthority("PERMISSION_CREATE_EDIT_USER")
                        .requestMatchers(HttpMethod.GET, "/permissions").hasAuthority("PERMISSION_VIEW_USER")

                        .requestMatchers("/admin/**").hasAuthority("PERMISSION_CREATE_EDIT_USER")
                        .anyRequest().authenticated()

                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder (){
        return new BCryptPasswordEncoder();
    }

}

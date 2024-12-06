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

                        .requestMatchers(HttpMethod.GET, "/categories/**").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/categories/**").hasAuthority("PERMISSION_CATEGORY_EDIT")
                        .requestMatchers(HttpMethod.POST, "/categories/**").hasAuthority("PERMISSION_CATEGORY_CREATE")
                        .requestMatchers(HttpMethod.DELETE, "/categories/**").hasAuthority("PERMISSION_CATEGORY_DELETE")

                        .requestMatchers(HttpMethod.GET, "/brands/**").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/brands/**").hasAuthority("PERMISSION_BRAND_EDIT")
                        .requestMatchers(HttpMethod.POST, "/brands/**").hasAuthority("PERMISSION_BRAND_CREATE")
                        .requestMatchers(HttpMethod.DELETE, "/brands/**").hasAuthority("PERMISSION_BRAND_DELETE")

                        .requestMatchers(HttpMethod.GET, "/products/**").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/products/**").hasAuthority("PERMISSION_PRODUCT_EDIT")
                        .requestMatchers(HttpMethod.POST, "/products").hasAuthority("PERMISSION_PRODUCT_CREATE")
                        .requestMatchers(HttpMethod.DELETE, "/products/**").hasAuthority("PERMISSION_PRODUCT_DELETE")

                        .requestMatchers(HttpMethod.GET, "/users/**").hasAuthority("PERMISSION_USER_VIEW")
                        .requestMatchers(HttpMethod.PUT, "/users/**").hasAuthority("PERMISSION_USER_EDIT")
                        .requestMatchers(HttpMethod.POST, "/users").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/users/**").hasAuthority("PERMISSION_USER_DELETE")

                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/admins").hasAuthority("PERMISSION_USER_CREATE")

                        .requestMatchers(HttpMethod.GET, "/roles/**").hasAuthority("PERMISSION_ROLE_VIEW")
                        .requestMatchers(HttpMethod.PUT, "/roles/**").hasAuthority("PERMISSION_ROLE_EDIT")
                        .requestMatchers(HttpMethod.POST, "/roles").hasAuthority("PERMISSION_ROLE_CREATE")
                        .requestMatchers(HttpMethod.DELETE, "/roles/**").hasAuthority("PERMISSION_ROLE_EDIT")

                        .requestMatchers(HttpMethod.GET, "/permissions").hasAuthority("PERMISSION_ROLE_VIEW")

                        .requestMatchers("/admin/**").hasAuthority("PERMISSION_USER_CREATE")
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

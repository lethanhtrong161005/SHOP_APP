package com.example.SHOP_APP.config;

import com.example.SHOP_APP.enums.Role;
import com.example.SHOP_APP.filter.CustomerFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@EnableWebSecurity
@Configuration
public class SecurityConfig {
    @Autowired
    private CustomSecurityHandlers customSecurityHandlers;
    @Autowired
    private CustomerFilter customerFilter;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(cors -> {})
                .authorizeHttpRequests(request -> {
                    request.requestMatchers("/api/auth/**").permitAll();
                    request.requestMatchers(HttpMethod.POST, "/api/auth/social/callback").permitAll();
                    request.requestMatchers(HttpMethod.GET, "/api/products/all").permitAll();
                    request.requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll();

                    request.requestMatchers(HttpMethod.DELETE,"/api/products/delete/**").hasRole(Role.ADMIN.name());

                    request.anyRequest().authenticated();
                })
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(customSecurityHandlers)
                        .accessDeniedHandler(customSecurityHandlers))
                .addFilterBefore(customerFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

}

package com.example.SHOP_APP.filter;


import com.example.SHOP_APP.config.CustomSecurityHandlers;
import com.example.SHOP_APP.entities.Account;
import com.example.SHOP_APP.exception.JwtAuthenticationException;
import com.example.SHOP_APP.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class CustomerFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomSecurityHandlers customSecurityHandlers;

    @Autowired
    private CustomerUserDetail userDetailService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        try{

            Claims claims = jwtUtil.getClaimsFromToken(token);

            if (jwtUtil.isTokenExpired(token)) {
                customSecurityHandlers.commence(
                        request,
                        response,
                        new JwtAuthenticationException("JWT token has expired")
                );
                return;
            }

            String email = claims.getSubject();
            String role = claims.get("role", String.class);

            if (email == null || role == null) {
                customSecurityHandlers.commence(
                        request,
                        response,
                        new JwtAuthenticationException("Token invalid: missing email or role")
                );
                return;
            }

            UserDetails userDetails = userDetailService.loadUserByUsername(email);

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);

        } catch (Exception e) {
            customSecurityHandlers.commence(
                    request,
                    response,
                    new JwtAuthenticationException("Invalid JWT: " + e.getMessage())
            );
        }
    }


}

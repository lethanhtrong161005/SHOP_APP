package com.example.SHOP_APP.utils;

import com.example.SHOP_APP.entities.Account;
import com.example.SHOP_APP.exception.CustomBusinessException;
import com.example.SHOP_APP.filter.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthUtil {
    public Account getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new CustomBusinessException("User is not authenticated");
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof CustomUserDetails) {
            return ((CustomUserDetails) principal).getAccount();
        }
        throw new CustomBusinessException("Invalid principal type");
    }

}

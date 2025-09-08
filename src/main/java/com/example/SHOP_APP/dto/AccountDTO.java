package com.example.SHOP_APP.dto;

import com.example.SHOP_APP.enums.Provider;
import com.example.SHOP_APP.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDTO {
    private Long id;
    private String email;
    private String fullName;
    private String avatarUrl;
    private Role role;
    private int status;
    private Provider provider;
}

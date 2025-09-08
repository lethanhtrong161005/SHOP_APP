package com.example.SHOP_APP.dto;

import lombok.Data;

@Data
public class GoogleUserInfoDTO {
    private String id;
    private String email;
    private String name;
    private String picture;
    private Boolean verified_email;
}

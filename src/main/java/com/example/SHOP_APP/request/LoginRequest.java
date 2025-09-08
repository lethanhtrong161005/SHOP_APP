package com.example.SHOP_APP.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = "Email must not be blank")
    @Email(message = "Email is not valid")
    private String email;

    @NotBlank(message = "Password must not be blank")
    private String password;

}

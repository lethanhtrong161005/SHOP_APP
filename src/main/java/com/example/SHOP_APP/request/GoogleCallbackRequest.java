package com.example.SHOP_APP.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GoogleCallbackRequest {

    @NotBlank(message = "Code must not be blank")
    private String code;

}

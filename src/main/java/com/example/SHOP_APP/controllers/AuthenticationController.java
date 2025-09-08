package com.example.SHOP_APP.controllers;

import com.example.SHOP_APP.request.LoginRequest;
import com.example.SHOP_APP.request.GoogleCallbackRequest;
import com.example.SHOP_APP.response.BaseResponse;
import com.example.SHOP_APP.response.LoginResponse;
import com.example.SHOP_APP.services.auth.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/local-login")
    public ResponseEntity<?> localLogin(
            @RequestBody @Valid LoginRequest loginRequest
    ){
        BaseResponse<LoginResponse> loginResponse = authenticationService.localLogin(loginRequest);
        return ResponseEntity.status(loginResponse.getStatus()).body(loginResponse);
    }

    @GetMapping("/social")
    public ResponseEntity<?> googleAuth(){
        BaseResponse<String> response = authenticationService.generateGoogleAuthUrl();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/social/callback")
    public ResponseEntity<?> callbackGoogleAuth(
        @RequestBody @Valid GoogleCallbackRequest googleCallbackRequest
    ){
        BaseResponse<LoginResponse> response = authenticationService.googleLogin(googleCallbackRequest);
        return ResponseEntity.status(response.getStatus()).body(response);
    }



}

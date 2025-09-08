package com.example.SHOP_APP.services.auth;


import com.example.SHOP_APP.request.LoginRequest;
import com.example.SHOP_APP.request.GoogleCallbackRequest;
import com.example.SHOP_APP.response.BaseResponse;
import com.example.SHOP_APP.response.LoginResponse;

public interface AuthenticationService {
    BaseResponse<LoginResponse> localLogin(LoginRequest loginRequest);
    BaseResponse<String> generateGoogleAuthUrl();
    BaseResponse<LoginResponse> googleLogin(GoogleCallbackRequest googleCallbackRequest);
}

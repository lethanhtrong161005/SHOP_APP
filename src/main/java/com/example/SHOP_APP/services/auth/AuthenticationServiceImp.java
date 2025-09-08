package com.example.SHOP_APP.services.auth;

import com.example.SHOP_APP.dto.GoogleUserInfoDTO;
import com.example.SHOP_APP.entities.Account;
import com.example.SHOP_APP.enums.Provider;
import com.example.SHOP_APP.enums.Role;
import com.example.SHOP_APP.exception.CustomBusinessException;
import com.example.SHOP_APP.repository.AccountRepository;
import com.example.SHOP_APP.request.GoogleCallbackRequest;
import com.example.SHOP_APP.request.LoginRequest;
import com.example.SHOP_APP.response.BaseResponse;
import com.example.SHOP_APP.response.LoginResponse;
import com.example.SHOP_APP.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import java.util.Map;
import java.util.Optional;

@Service
public class AuthenticationServiceImp implements AuthenticationService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;
    @Value("${google.authUri}")
    private String googleAuthUri;
    @Value("${google.clientId}")
    private String googleClientId;
    @Value("${google.clientSecret}")
    private String googleClientSecret;
    @Value("${google.redirectUri}")
    private String googleRedirectUri;
    @Value("${google.tokenUri}")
    private String googleTokenUri;
    @Value("${google.userInfoUri}")
    private String googleUserInfoUri;

    @Override
    public BaseResponse<LoginResponse> localLogin(LoginRequest request) {
        BaseResponse<LoginResponse> baseResponse = new BaseResponse();

        Optional<Account> account = accountRepository.findByEmail(request.getEmail());
        if(account.isPresent()){
            if(passwordEncoder.matches(request.getPassword(),account.get().getPassword())){
                String accessToken = jwtUtil.generateToken(account.get());
                String refreshToken = jwtUtil.generateRefreshToken(account.get());
                LoginResponse loginResponse = new LoginResponse();
                loginResponse.setAccessToken(accessToken);
                loginResponse.setRefreshToken(refreshToken);
                baseResponse.setData(loginResponse);
                baseResponse.setMessage("Login Successful");
                baseResponse.setSuccess(true);
                baseResponse.setStatus(200);
            }else{
                throw new CustomBusinessException("Wrong password");
            }
        }else{
            throw new CustomBusinessException("Email not found");
        }
        return baseResponse;
    }

    @Override
    public BaseResponse<String> generateGoogleAuthUrl() {
        BaseResponse<String> baseResponse = new BaseResponse();
        baseResponse.setSuccess(true);
        baseResponse.setStatus(200);
        String url = String.format(
                        "%s?client_id=%s&redirect_uri=%s&response_type=code&scope=openid%%20email%%20profile&access_type=offline&prompt=consent",
                        googleAuthUri,
                        googleClientId,
                        googleRedirectUri);
        baseResponse.setData(url);
        return baseResponse;
    }

    @Override
    public BaseResponse<LoginResponse> googleLogin(GoogleCallbackRequest googleCallbackRequest) {
        BaseResponse<LoginResponse> baseResponse = new BaseResponse();
        try{
            String rawCode = URLDecoder.decode(googleCallbackRequest.getCode(), StandardCharsets.UTF_8);
            Map<String,Object> tokenResponse = exchangeCodeForToken(rawCode);

            String accessToken = (String) tokenResponse.get("access_token");

            GoogleUserInfoDTO accountInfo = getAccountInfo(accessToken);

            Account account = upsertUser(accountInfo);

            LoginResponse loginResponse = new LoginResponse();
            String jwtAccessToken = jwtUtil.generateToken(account);
            String jwtRefreshToken = jwtUtil.generateRefreshToken(account);
            loginResponse.setAccessToken(jwtAccessToken);
            loginResponse.setRefreshToken(jwtRefreshToken);
            baseResponse.setData(loginResponse);
            baseResponse.setMessage("Login Successful");
            baseResponse.setSuccess(true);
            baseResponse.setStatus(200);
        }catch (Exception e){
            throw new CustomBusinessException(e.getMessage());
        }
        return baseResponse;
    }

    // change code -> access_token
    private Map<String, Object> exchangeCodeForToken(String code){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("code", code);
        formData.add("client_id", googleClientId);
        formData.add("client_secret", googleClientSecret);
        formData.add("redirect_uri", googleRedirectUri);
        formData.add("grant_type", "authorization_code");

        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForObject(googleTokenUri, new HttpEntity<>(formData, headers), Map.class);
    }

    private Account upsertUser(GoogleUserInfoDTO userInfo){
        return accountRepository.findByEmail(userInfo.getEmail()).orElseGet(() -> {
            Account acc = new Account();
            acc.setEmail(userInfo.getEmail());
            acc.setFullName(userInfo.getName());
            acc.setProvider(Provider.GOOGLE);
            acc.setProviderId(userInfo.getId());
            acc.setAvatarUrl(userInfo.getPicture());
            acc.setRole(Role.USER);
            acc.setStatus(1);
            return accountRepository.save(acc);
        });
    }


    private GoogleUserInfoDTO getAccountInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        RestTemplate restTemplate = new RestTemplate();

        return restTemplate.exchange(
                googleUserInfoUri,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                GoogleUserInfoDTO.class
        ).getBody();
    }


}

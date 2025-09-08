package com.example.SHOP_APP.utils;

import com.example.SHOP_APP.entities.Account;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {
    @Value("${jwt.key}")
    private String jwtKey;

    @Value("${jwt.expiration}")
    private Long jwtExpiration;

    @Value("${jwt.refresh-expiration}")
    private Long jwtRefresh;

    private SecretKey getSignInKey(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtKey));
    }


    // ===== Generate Token =====
    public String generateToken(Account account){
        String token = "";
        token = Jwts.builder()
                .id(account.getId().toString())
                .subject(account.getEmail())
                .claim("role", account.getRole().name())
                .claim("name", account.getFullName())
                .claim("provider", account.getProvider())
                .claim("avatar", account.getAvatarUrl())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSignInKey())
                .compact();
        return token;
    }

    public String generateRefreshToken(Account account) {
        return Jwts.builder()
                .id(account.getId().toString())
                .subject(account.getEmail())
                .claim("provider", account.getProvider())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtRefresh))
                .signWith(getSignInKey())
                .compact();
    }

    // ===== Extract Claims =====
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims getAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // ===== Validation =====
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public boolean validateToken(String token, Account account) {
        final String email = extractEmail(token);
        return (email.equals(account.getEmail()) && !isTokenExpired(token));
    }

    public boolean validateToken(String token) {
        try {
            getAllClaims(token);
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    public Claims getClaimsFromToken(String token) {
        return getAllClaims(token);
    }

}

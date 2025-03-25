package com.krittawat.groomingapi.helper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret-key}")
    private String secretKey;  // secretKey ในไฟล์ application.properties หรือ .env

    // สร้าง Key จาก secretKey
    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // สร้าง JWT Token
    public String generateToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 3600000)) // หมดอายุหลัง 1 ชั่วโมง
                .signWith(getSigningKey())  // ใช้ Key จาก secretKey
                .compact();
    }

    // ตรวจสอบ JWT Token
    public Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())  // ใช้ Key แทน String
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // ตรวจสอบว่า JWT Token หมดอายุหรือไม่
    public boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    // ตรวจสอบว่า token valid หรือไม่
    public boolean validateToken(String token, String username) {
        return (username.equals(extractClaims(token).getSubject()) && !isTokenExpired(token));
    }

    // สร้าง Refresh Token (จะหมดอายุยาวกว่า JWT Token)
    public String generateRefreshToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 86400000)) // หมดอายุหลัง 24 ชั่วโมง
                .signWith(getSigningKey())  // ใช้ Key จาก secretKey
                .compact();
    }
}

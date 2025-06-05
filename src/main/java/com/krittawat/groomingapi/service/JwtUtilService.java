package com.krittawat.groomingapi.service;

import com.krittawat.groomingapi.config.SecurityConstants;
import com.krittawat.groomingapi.error.TokenExpiredException;
import com.krittawat.groomingapi.error.UnauthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtUtilService {

    private SecretKey getSigningKey() {
        return Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }

    public String generateToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION))
                .signWith(getSigningKey())
                .compact();
    }

    public Claims extractClaims(String token) throws UnauthorizedException {
        try{
            return Jwts.parser()
                    .verifyWith(getSigningKey())  // ใช้ Key แทน String
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            throw new UnauthorizedException("Invalid token");
        }
    }

    public void isTokenExpired(Claims claims) throws TokenExpiredException {
        if (claims.getExpiration().before(new Date())){
            throw new TokenExpiredException("token is expired");
        }
    }

    public boolean isTokenExpired(String token) throws UnauthorizedException {
        if (extractClaims(token).getExpiration().before(new Date())){
            return true;
        }
        return false;
    }

    public Claims validateToken(String token) throws UnauthorizedException, TokenExpiredException {
        Claims claims = extractClaims(token);
        isTokenExpired(claims);
        return claims;
    }

    // สร้าง Refresh Token (จะหมดอายุยาวกว่า JWT Token)
    public String generateRefreshToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(getSigningKey())  // ใช้ Key จาก secretKey
                .compact();
    }

    public String extractUsername(String token) throws UnauthorizedException {
        return extractClaims(token).getSubject();
    }
}

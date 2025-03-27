package com.krittawat.groomingapi.config;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class SecurityConstants {
    public static final String[] PUBLIC_ENDPOINTS = {"/auth/**"};
    public static String SECRET_KEY;
    public static long EXPIRATION;

    @Value("${jwt.secret-key}")
    private String SECRET_KEY_VALUE;

    @Value("${jwt.expiration}")
    private long TOKEN_EXPIRATION_VALUE;
    @PostConstruct
    public void init() {
        SECRET_KEY = SECRET_KEY_VALUE;
        EXPIRATION = TOKEN_EXPIRATION_VALUE;
    }
}

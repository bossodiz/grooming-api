package com.krittawat.groomingapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests()
                .anyRequest().permitAll() // ให้ทุกหน้าสามารถเข้าถึงได้โดยไม่ต้องล็อกอิน
                .and()
                .formLogin().disable(); // ปิดฟอร์มล็อกอิน
        return http.build();
    }
}

package com.krittawat.groomingapi.service;

import com.krittawat.groomingapi.datasource.entity.User;
import com.krittawat.groomingapi.datasource.repository.UserRepository;
import com.krittawat.groomingapi.helper.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    // ใช้ BCryptPasswordEncoder ในการเข้ารหัสและตรวจสอบ password
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Login API
    public String login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // ตรวจสอบ password
        if (passwordEncoder.matches(password, user.getPassword())) {
            return jwtUtil.generateToken(username); // ส่ง JWT token
        } else {
            throw new RuntimeException("Invalid credentials");
        }
    }

    // Register user API
    public void register(String username, String password) {
        // เข้ารหัส password ก่อนบันทึกในฐานข้อมูล
        String encodedPassword = passwordEncoder.encode(password);

        // บันทึกผู้ใช้ใหม่
        User user = new User();
        user.setUsername(username);
        user.setPassword(encodedPassword);  // เก็บ password ที่ถูกเข้ารหัส
        userRepository.save(user);
    }

}

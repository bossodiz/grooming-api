package com.krittawat.groomingapi.controller;

import com.krittawat.groomingapi.controller.request.LoginRequest;
import com.krittawat.groomingapi.controller.request.RefreshTokenRequest;
import com.krittawat.groomingapi.controller.request.RegisterRequest;
import com.krittawat.groomingapi.controller.response.AuthResponse;
import com.krittawat.groomingapi.controller.response.Response;
import com.krittawat.groomingapi.error.BadRequestException;
import com.krittawat.groomingapi.error.DataNotFoundException;
import com.krittawat.groomingapi.error.TokenExpiredException;
import com.krittawat.groomingapi.error.UnauthorizedException;
import com.krittawat.groomingapi.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public Response register(@RequestBody RegisterRequest request) throws BadRequestException, DataNotFoundException {
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) throws BadRequestException {
        return authService.login(request);
    }

    @PostMapping("/refresh-token")
    public AuthResponse refreshToken(@RequestBody RefreshTokenRequest request) throws DataNotFoundException, UnauthorizedException, TokenExpiredException {
        return authService.refreshToken(request);
    }
}

package com.krittawat.groomingapi.service;

import com.krittawat.groomingapi.controller.request.LoginRequest;
import com.krittawat.groomingapi.controller.request.RefreshTokenRequest;
import com.krittawat.groomingapi.controller.request.RegisterRequest;
import com.krittawat.groomingapi.controller.response.AuthResponse;
import com.krittawat.groomingapi.controller.response.Response;
import com.krittawat.groomingapi.datasource.entity.EUser;
import com.krittawat.groomingapi.datasource.service.RoleService;
import com.krittawat.groomingapi.datasource.service.UserService;
import com.krittawat.groomingapi.error.BadRequestException;
import com.krittawat.groomingapi.error.DataNotFoundException;
import com.krittawat.groomingapi.error.TokenExpiredException;
import com.krittawat.groomingapi.error.UnauthorizedException;
import com.krittawat.groomingapi.service.model.RoleProfile;
import com.krittawat.groomingapi.service.model.UserProfile;
import com.krittawat.groomingapi.utils.UtilService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final RoleService roleService;
    private final UserService userService;

    private final JwtUtilService jwtUtilService;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthResponse login(LoginRequest request) throws BadRequestException {
        try{
            EUser user = userService.findByUsername(request.getUsername().trim());
            if (!passwordEncoder.matches(request.getPassword().trim(), user.getPassword())) {
                throw new BadRequestException("Invalid username or password");
            }
            return AuthResponse.builder()
                    .token(jwtUtilService.generateToken(request.getUsername()))
                    .refreshToken(jwtUtilService.generateRefreshToken(request.getUsername()))
                    .profile(UserProfile.builder()
                            .id(user.getId())
                            .firstname(user.getFirstname())
                            .lastname(user.getLastname())
                            .nickname(user.getNickname())
                            .username(user.getUsername())
                            .email(user.getEmail())
                            .phone1(user.getPhone1())
                            .phone2(user.getPhone2())
                            .role(RoleProfile.builder()
                                    .id(user.getRole().getId())
                                    .name(user.getRole().getName())
                                    .build())
                            .build())
                    .build();
        } catch (DataNotFoundException e){
            throw new BadRequestException("Invalid username or password");
        }

    }

    @Transactional
    public Response register(RegisterRequest request) throws BadRequestException, DataNotFoundException {
        if (!request.getPassword().equals(request.getConfirmPassword())){
            throw new BadRequestException("Confirm password does not match");
        }
        if (userService.existsByUsername(request.getUsername().trim())) {
            throw new BadRequestException("Username is already taken");
        }
        EUser user = new EUser();
        user.setUsername(UtilService.trimOrNull(request.getUsername()));
        user.setPassword(passwordEncoder.encode(UtilService.trimOrNull(request.getPassword())));
        user.setFirstname(UtilService.trimOrNull(request.getFirstname()));
        user.setLastname(UtilService.trimOrNull(request.getLastname()));
        user.setNickname(UtilService.trimOrNull(request.getNickname()));
        user.setEmail(UtilService.trimOrNull(request.getEmail()));
        user.setPhone1(UtilService.trimOrNull(request.getPhone1()));
        user.setPhone2(UtilService.trimOrNull(request.getPhone2()));
        user.setRole(roleService.getRoleAdmin());
        userService.save(user);
        return Response.builder()
                .code(200)
                .data("User registered successfully")
                .build();
    }

    public AuthResponse refreshToken(RefreshTokenRequest request) throws DataNotFoundException, UnauthorizedException, TokenExpiredException {
        // ตรวจสอบว่า refresh token ถูกต้อง
        if (request.getRefreshToken() != null && jwtUtilService.isTokenExpired(request.getRefreshToken())) {
            throw new UnauthorizedException("Refresh Token is expired");
        }
        String username = jwtUtilService.extractUsername(request.getRefreshToken());
        EUser user = userService.findByUsername(username);
        return AuthResponse.builder()
                .token(jwtUtilService.generateToken(username))
                .refreshToken(jwtUtilService.generateRefreshToken(username))
                .profile(UserProfile.builder()
                        .id(user.getId())
                        .firstname(user.getFirstname())
                        .lastname(user.getLastname())
                        .nickname(user.getNickname())
                        .username(user.getUsername())
                        .email(user.getEmail())
                        .phone1(user.getPhone1())
                        .phone2(user.getPhone2())
                        .role(RoleProfile.builder()
                                .id(user.getRole().getId())
                                .name(user.getRole().getName())
                                .description(user.getRole().getDescription())
                                .build())
                        .build())
                .build();
    }
}

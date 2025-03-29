package com.krittawat.groomingapi.helper;

import com.krittawat.groomingapi.config.SecurityConstants;
import com.krittawat.groomingapi.error.TokenExpiredException;
import com.krittawat.groomingapi.error.UnauthorizedException;
import com.krittawat.groomingapi.service.JwtUtilService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtilService jwtUtilService;  // อ้างอิงถึง JwtUtil ที่คุณสร้างไว้

    private String getTokenFromRequest(HttpServletRequest request) {
        // ตรวจสอบว่า Authorization header มี "Bearer" หรือไม่
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);  // ตัด "Bearer " ออก
        }
        return null;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (isPublicEndpoint(request.getRequestURI())){
            filterChain.doFilter(request, response);
            return;
        }
        String token = getTokenFromRequest(request);
        if (token == null){
            // ถ้าไม่มี Token ส่ง HTTP 401
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);  // HTTP 401
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"missing token\"}");
            return;
        }
        try {
            Claims claims = jwtUtilService.validateToken(token);
            String username = claims.getSubject();
            // สร้าง Authentication object
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    username, null, null);  // คุณสามารถเพิ่ม Authorities ได้ที่นี่ถ้าจำเป็น
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            // กำหนดให้ SecurityContext มีข้อมูลการตรวจสอบตัวตน
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        } catch (UnauthorizedException | TokenExpiredException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);  // HTTP 401
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    private boolean isPublicEndpoint(String requestURI) {
        return Arrays.stream(SecurityConstants.PUBLIC_ENDPOINTS)
                .anyMatch(endpoint -> requestURI.replaceFirst("^/api", "").equals(endpoint));
    }

}
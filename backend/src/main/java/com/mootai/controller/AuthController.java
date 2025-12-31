package com.mootai.controller;

import com.mootai.dto.ApiResponse;
import com.mootai.dto.AuthResponse;
import com.mootai.dto.LoginRequest;
import com.mootai.dto.RegisterRequest;
import com.mootai.entity.User;
import com.mootai.service.AuthService;
import com.mootai.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final AuthService authService;
    private final UserService userService;
    
    @PostMapping("/register")
    public ApiResponse<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        try {
            User user = userService.register(request);
            // 注册后自动登录
            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setUsername(user.getUsername());
            loginRequest.setPassword(request.getPassword());
            AuthResponse authResponse = authService.login(loginRequest);
            return ApiResponse.success("注册成功", authResponse);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    @PostMapping("/login")
    public ApiResponse<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        try {
            AuthResponse response = authService.login(request);
            return ApiResponse.success("登录成功", response);
        } catch (Exception e) {
            return ApiResponse.error(401, e.getMessage());
        }
    }
}


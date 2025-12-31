package com.mootai.service;

import com.mootai.dto.AuthResponse;
import com.mootai.dto.LoginRequest;
import com.mootai.entity.User;
import com.mootai.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    
    public AuthResponse login(LoginRequest request) {
        // 查找用户
        User user = userService.findByUsername(request.getUsername());
        
        // 验证密码
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("密码错误");
        }
        
        // 生成JWT token
        String token = jwtUtil.generateToken(user.getUsername());
        
        return new AuthResponse(token, user.getUsername(), user.getId());
    }
}


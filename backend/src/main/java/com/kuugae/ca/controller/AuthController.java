package com.kuugae.ca.controller;

import com.kuugae.ca.annotation.AuditOperation;
import com.kuugae.ca.dto.*;
import com.kuugae.ca.entity.User;
import com.kuugae.ca.security.JwtTokenProvider;
import com.kuugae.ca.security.UserPrincipal;
import com.kuugae.ca.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtTokenProvider tokenProvider;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Map<String, Object>>> login(@Valid @RequestBody LoginRequest request) {
        User user = userService.login(request.getEmail(), request.getPassword());

        String token = tokenProvider.generateToken(user.getEmail());

        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("user", userService.convertToResponse(user));

        return ResponseEntity.ok(ApiResponse.success("登录成功", data));
    }

    @PostMapping("/register")
    @AuditOperation(type = "用户管理", targetType = "用户注册", targetId = "#request.email")
    public ResponseEntity<ApiResponse<UserResponse>> register(@Valid @RequestBody RegisterRequest request) {
        User user = userService.register(
                request.getEmail(),
                request.getUsername(),
                request.getPassword()
        );

        return ResponseEntity.ok(ApiResponse.success("注册成功", userService.convertToResponse(user)));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userService.findByEmail(email);

        return ResponseEntity.ok(ApiResponse.success(userService.convertToResponse(user)));
    }

    @PostMapping("/logout")
    @AuditOperation(type = "用户管理", targetType = "用户登出", targetId = "logout")
    public ResponseEntity<ApiResponse<Void>> logout() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(ApiResponse.success("退出成功", null));
    }
}

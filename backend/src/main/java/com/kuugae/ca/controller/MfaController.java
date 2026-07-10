package com.kuugae.ca.controller;

import com.kuugae.ca.annotation.AuditOperation;
import com.kuugae.ca.dto.ApiResponse;
import com.kuugae.ca.dto.MfaSetupRequest;
import com.kuugae.ca.dto.MfaSetupResponse;
import com.kuugae.ca.dto.MfaVerifyRequest;
import com.kuugae.ca.security.UserPrincipal;
import com.kuugae.ca.service.MfaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/mfa")
@RequiredArgsConstructor
public class MfaController {

    private final MfaService mfaService;

    @PostMapping("/setup")
    public ResponseEntity<ApiResponse<MfaSetupResponse>> setupMfa() {
        String email = getCurrentUserEmail();
        
        String secret = mfaService.generateMfaSecret(email);
        String issuer = "CaSystem";
        String qrCodeUri = mfaService.generateTotpUri(email, secret, issuer);
        String manualEntryKey = secret.replaceAll("\\s", "");

        MfaSetupResponse response = MfaSetupResponse.builder()
            .secret(secret)
            .qrCodeUri(qrCodeUri)
            .manualEntryKey(manualEntryKey)
            .build();

        log.info("用户 {} 初始化 MFA", email);
        return ResponseEntity.ok(ApiResponse.success("MFA 初始化成功", response));
    }

    @PostMapping("/setup/verify")
    @AuditOperation(type = "系统设置", targetType = "MFA 验证", targetId = "verify")
    public ResponseEntity<ApiResponse<Map<String, Object>>> verifyMfaSetup(
            @Valid @RequestBody MfaSetupRequest request) {
        String email = getCurrentUserEmail();
        
        boolean success = mfaService.verifyAndEnableMfa(email, request.getCode());

        Map<String, Object> result = new HashMap<>();
        result.put("enabled", success);

        if (success) {
            log.info("用户 {} 启用 MFA 成功", email);
            return ResponseEntity.ok(ApiResponse.success("MFA 启用成功", result));
        } else {
            log.warn("用户 {} 启用 MFA 失败", email);
            return ResponseEntity.badRequest()
                .body(ApiResponse.error(400, "验证码错误"));
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<ApiResponse<Map<String, Object>>> verifyMfa(
            @Valid @RequestBody MfaVerifyRequest request) {
        String email = getCurrentUserEmail();
        
        boolean success = mfaService.verifyTotp(email, request.getCode());

        Map<String, Object> result = new HashMap<>();
        result.put("valid", success);

        if (success) {
            return ResponseEntity.ok(ApiResponse.success("验证码正确", result));
        } else {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error(400, "验证码错误"));
        }
    }

    @PostMapping("/disable")
    @AuditOperation(type = "系统设置", targetType = "MFA 禁用", targetId = "disable")
    public ResponseEntity<ApiResponse<Map<String, Object>>> disableMfa(
            @Valid @RequestBody MfaVerifyRequest request) {
        String email = getCurrentUserEmail();
        
        boolean success = mfaService.disableMfa(email, request.getCode());

        Map<String, Object> result = new HashMap<>();
        result.put("disabled", success);

        if (success) {
            log.info("用户 {} 禁用 MFA 成功", email);
            return ResponseEntity.ok(ApiResponse.success("MFA 禁用成功", result));
        } else {
            log.warn("用户 {} 禁用 MFA 失败", email);
            return ResponseEntity.badRequest()
                .body(ApiResponse.error(400, "操作失败"));
        }
    }

    @GetMapping("/status")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getMfaStatus() {
        String email = getCurrentUserEmail();
        
        boolean enabled = mfaService.isMfaEnabled(email);
        boolean setupRequired = mfaService.isMfaSetupRequired(email);

        Map<String, Object> result = new HashMap<>();
        result.put("enabled", enabled);
        result.put("setupRequired", setupRequired);

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    private String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal) {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            return userPrincipal.getEmail();
        }
        throw new RuntimeException("未找到当前用户");
    }
}

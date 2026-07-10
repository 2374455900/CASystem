package com.kuugae.ca.controller;

import com.kuugae.ca.dto.ApiResponse;
import com.kuugae.ca.dto.CertificateInfo;
import com.kuugae.ca.dto.ValidationResult;
import com.kuugae.ca.service.CertificateValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/tools")
@RequiredArgsConstructor
public class CertificateToolsController {

    private final CertificateValidationService validationService;

    @PostMapping("/parse")
    public ResponseEntity<ApiResponse<CertificateInfo>> parseCertificate(
            @RequestParam("file") MultipartFile certFile) {
        try {
            byte[] certBytes = certFile.getBytes();
            CertificateInfo info = validationService.parseCertificate(certBytes);
            
            log.info("证书解析成功：{}", info.getSerialNumber());
            return ResponseEntity.ok(ApiResponse.success("证书解析成功", info));
            
        } catch (Exception e) {
            log.error("证书解析失败", e);
            return ResponseEntity.badRequest()
                .body(ApiResponse.error(400, "证书解析失败：" + e.getMessage()));
        }
    }

    @PostMapping("/verify-chain")
    public ResponseEntity<ApiResponse<ValidationResult>> verifyCertificateChain(
            @RequestParam("cert") MultipartFile certFile,
            @RequestParam(value = "chain", required = false) List<MultipartFile> chainFiles) {
        try {
            byte[] certBytes = certFile.getBytes();
            List<byte[]> chainBytes = new ArrayList<>();

            if (chainFiles != null) {
                for (MultipartFile chainFile : chainFiles) {
                    chainBytes.add(chainFile.getBytes());
                }
            }

            ValidationResult result = validationService.validateCertificateChain(certBytes, chainBytes);

            if (result.getValid()) {
                return ResponseEntity.ok(ApiResponse.success("证书链验证通过", result));
            } else {
                return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, result.getMessage()));
            }

        } catch (Exception e) {
            log.error("证书链验证失败", e);
            return ResponseEntity.badRequest()
                .body(ApiResponse.error(400, "证书链验证失败：" + e.getMessage()));
        }
    }

    @PostMapping("/check-status")
    public ResponseEntity<ApiResponse<Map<String, Object>>> checkCertificateStatus(
            @RequestParam("file") MultipartFile certFile) {
        try {
            byte[] certBytes = certFile.getBytes();
            CertificateInfo.CertificateStatus status = validationService.checkCertificateStatus(certBytes);

            Map<String, Object> result = new HashMap<>();
            result.put("status", status);
            result.put("statusSummary", buildStatusSummary(status));

            return ResponseEntity.ok(ApiResponse.success("证书状态检查完成", result));

        } catch (Exception e) {
            log.error("证书状态检查失败", e);
            return ResponseEntity.badRequest()
                .body(ApiResponse.error(400, "证书状态检查失败：" + e.getMessage()));
        }
    }

    @GetMapping("/decode/{serialNumber}")
    public ResponseEntity<ApiResponse<CertificateInfo>> decodeCertificate(
            @PathVariable String serialNumber) {
        // 从数据库查询证书并解析
        // 这里需要根据实际业务逻辑实现
        return ResponseEntity.ok(ApiResponse.success("证书解码成功", null));
    }

    private String buildStatusSummary(CertificateInfo.CertificateStatus status) {
        if (!status.getValid()) {
            if (status.getExpired()) {
                return "证书已过期";
            } else if (status.getNotYetValid()) {
                return "证书尚未生效";
            }
        }
        
        long days = status.getDaysUntilExpiry();
        if (days <= 7) {
            return "证书即将到期（" + days + "天）";
        } else if (days <= 30) {
            return "证书将在 " + days + " 天后到期";
        } else {
            return "证书有效";
        }
    }
}

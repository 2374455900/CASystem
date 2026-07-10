package com.kuugae.ca.controller;

import com.kuugae.ca.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/public")
@RequiredArgsConstructor
public class PublicKeyController {

    @Value("${app.certificate.key-directory}")
    private String keyDirectory;

    @GetMapping("/key")
    public ResponseEntity<ApiResponse<Map<String, String>>> getPublicKey() {
        Map<String, String> result = new HashMap<>();

        try {
            String publicKeyPath = keyDirectory + "/PublicKey.pem";
            String publicKeyContent = new String(
                    java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(publicKeyPath))
            );

            result.put("publicKey", publicKeyContent);
            return ResponseEntity.ok(ApiResponse.success(result));
        } catch (Exception e) {
            result.put("publicKey", "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA0Z3VS5JJcds3xfn/ygWyF8PbnGy0AHt0xMy4SHTX0wIDAQAB");
            return ResponseEntity.ok(ApiResponse.success(result));
        }
    }

    @GetMapping("/health")
    public ResponseEntity<ApiResponse<Map<String, String>>> health() {
        Map<String, String> data = new HashMap<>();
        data.put("status", "UP");
        data.put("timestamp", String.valueOf(System.currentTimeMillis()));
        return ResponseEntity.ok(ApiResponse.success(data));
    }
}

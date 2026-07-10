package com.kuugae.ca.service;

import com.kuugae.ca.entity.Certificate;
import com.kuugae.ca.entity.User;
import com.kuugae.ca.exception.BusinessException;
import com.kuugae.ca.repository.CertificateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.Signature;
import java.util.Base64;

@Slf4j
@Service
@RequiredArgsConstructor
public class SignatureGenerationService {

    private final CertificateRepository certificateRepository;
    private final UserService userService;

    /**
     * 使用用户私钥对数据进行签名
     * @param data 原始数据
     * @param serialNumber 证书序列号
     * @param username 用户名
     * @return 签名结果
     */
    public SignatureResult generateSignature(String data, String serialNumber, String username) {
        try {
            // 1. 验证证书所有权
            Certificate certificate = certificateRepository.findBySerialNumber(serialNumber)
                .orElseThrow(() -> new BusinessException(404, "证书不存在"));
            
            // 获取当前用户信息，用于兼容旧证书数据（旧证书可能存储的是邮箱）
            User currentUser = userService.findByUsername(username);
            String userEmail = currentUser.getEmail();
            String certUsername = certificate.getUsername();
            
            log.info("签名生成 - 验证所有权: 证书用户名='{}', 当前用户='{}', 当前邮箱='{}', 序列号='{}'", 
                certUsername, username, userEmail, serialNumber);
            
            // 证书用户名匹配当前用户名或当前邮箱
            if (!certUsername.equals(username) && !certUsername.equals(userEmail)) {
                log.warn("签名生成 - 所有权验证失败: 证书用户名='{}' 不匹配 当前用户='{}' 或 当前邮箱='{}'", 
                    certUsername, username, userEmail);
                throw new BusinessException(403, "无权使用该证书进行签名");
            }
            
            // 2. 检查证书状态
            if (!"使用中".equals(certificate.getState())) {
                throw new BusinessException(400, "证书状态无效，当前状态：" + certificate.getState());
            }
            
            // 3. 获取用户私钥
            String privateKeyPem = certificate.getUserPrivateKeyEncrypted();
            if (privateKeyPem == null || privateKeyPem.isEmpty()) {
                throw new BusinessException(404, "该证书没有关联的私钥");
            }
            
            // 4. 加载私钥
            PrivateKey privateKey = loadPrivateKey(privateKeyPem);
            
            // 5. 创建签名
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(privateKey);
            signature.update(data.getBytes(StandardCharsets.UTF_8));
            
            // 6. 生成签名并转为 Base64
            byte[] signatureBytes = signature.sign();
            String signatureBase64 = Base64.getEncoder().encodeToString(signatureBytes);
            
            // 7. 计算数据哈希
            String dataHash = calculateDataHash(data);
            
            log.info("用户 {} 使用证书 {} 成功生成签名", username, serialNumber);
            
            return SignatureResult.builder()
                .success(true)
                .signature(signatureBase64)
                .data(data)
                .dataHash(dataHash)
                .serialNumber(serialNumber)
                .subject(certificate.getOrganization())
                .algorithm("SHA256withRSA")
                .message("签名生成成功")
                .build();
                
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("生成签名失败", e);
            throw new BusinessException(500, "签名生成失败：" + e.getMessage());
        }
    }
    
    /**
     * 从 PEM 格式加载私钥
     */
    private PrivateKey loadPrivateKey(String pem) throws Exception {
        String cleaned = pem.replace("-----BEGIN PRIVATE KEY-----", "")
                           .replace("-----END PRIVATE KEY-----", "")
                           .replace("-----BEGIN RSA PRIVATE KEY-----", "")
                           .replace("-----END RSA PRIVATE KEY-----", "")
                           .replaceAll("\\s", "");
        
        byte[] keyBytes = Base64.getDecoder().decode(cleaned);
        java.security.spec.PKCS8EncodedKeySpec keySpec = 
            new java.security.spec.PKCS8EncodedKeySpec(keyBytes);
        java.security.KeyFactory keyFactory = java.security.KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }
    
    /**
     * 计算数据哈希值
     */
    private String calculateDataHash(String data) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02X", b));
            }
            return sb.toString();
        } catch (Exception e) {
            return "N/A";
        }
    }
    
    /**
     * 签名结果
     */
    @lombok.Builder
    @lombok.Data
    public static class SignatureResult {
        private boolean success;
        private String signature;
        private String data;
        private String dataHash;
        private String serialNumber;
        private String subject;
        private String algorithm;
        private String message;
    }
}

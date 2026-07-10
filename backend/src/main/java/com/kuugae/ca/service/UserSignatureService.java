package com.kuugae.ca.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import java.security.Signature;
import java.util.Base64;

@Slf4j
@Service
public class UserSignatureService {

    /**
     * 验证用户签名
     * @param data 原始数据
     * @param signatureBase64 Base64 编码的签名
     * @param userPublicKeyPem 用户公钥（PEM 格式）
     * @return 验证结果
     */
    public SignatureVerificationResult verifyUserSignature(
            String data,
            String signatureBase64,
            String userPublicKeyPem) {
        try {
            log.info("开始验证签名 - 数据长度: {}, 签名长度: {}", 
                data != null ? data.length() : 0, 
                signatureBase64 != null ? signatureBase64.length() : 0);
            
            // 解析用户公钥
            PublicKey publicKey = loadPublicKeyFromPem(userPublicKeyPem);
            log.debug("公钥解析成功");
            
            // 解码签名
            byte[] signatureBytes;
            try {
                signatureBytes = Base64.getDecoder().decode(signatureBase64);
                log.debug("签名解码成功，字节数: {}", signatureBytes.length);
            } catch (IllegalArgumentException e) {
                log.error("签名Base64解码失败: {}", e.getMessage());
                SignatureVerificationResult result = new SignatureVerificationResult();
                result.setValid(false);
                result.setMessage("签名数据格式错误，请确保是有效的Base64编码");
                result.setDataHash(calculateHash(data));
                return result;
            }
            
            // 验证签名
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initVerify(publicKey);
            signature.update(data.getBytes(StandardCharsets.UTF_8));
            
            boolean isValid = signature.verify(signatureBytes);
            
            SignatureVerificationResult result = new SignatureVerificationResult();
            result.setValid(isValid);
            result.setMessage(isValid ? "签名验证通过" : "签名验证失败，数据可能被篡改或证书不匹配");
            result.setDataHash(calculateHash(data));
            
            log.info("用户签名验证结果: {}, 数据哈希: {}", isValid ? "通过" : "失败", result.getDataHash());
            return result;
            
        } catch (Exception e) {
            log.error("验证用户签名失败", e);
            SignatureVerificationResult result = new SignatureVerificationResult();
            result.setValid(false);
            result.setMessage("签名验证失败：" + e.getMessage());
            return result;
        }
    }

    /**
     * 从 PEM 格式加载公钥
     */
    private PublicKey loadPublicKeyFromPem(String pem) throws Exception {
        String cleaned = pem.replace("-----BEGIN PUBLIC KEY-----", "");
        cleaned = cleaned.replace("-----END PUBLIC KEY-----", "");
        cleaned = cleaned.replaceAll("\\s", "");
        
        byte[] keyBytes = Base64.getDecoder().decode(cleaned);
        java.security.spec.X509EncodedKeySpec keySpec = 
            new java.security.spec.X509EncodedKeySpec(keyBytes);
        java.security.KeyFactory keyFactory = java.security.KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }

    /**
     * 计算数据哈希值
     */
    private String calculateHash(String data) {
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
     * 签名验证结果
     */
    public static class SignatureVerificationResult {
        private boolean valid;
        private String message;
        private String dataHash;

        public boolean isValid() {
            return valid;
        }

        public void setValid(boolean valid) {
            this.valid = valid;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getDataHash() {
            return dataHash;
        }

        public void setDataHash(String dataHash) {
            this.dataHash = dataHash;
        }
    }
}

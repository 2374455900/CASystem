package com.kuugae.ca.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

@Slf4j
@Service
public class UserKeyService {

    /**
     * 生成用户密钥对
     * @param algorithm 算法（RSA、EC 等）
     * @param keySize 密钥长度
     * @return 密钥对信息
     */
    public KeyPairInfo generateUserKeyPair(String algorithm, int keySize) {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithm);
            keyPairGenerator.initialize(keySize);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            String publicKeyPem = encodePublicKeyToPem(keyPair.getPublic());
            String privateKeyPem = encodePrivateKeyToPem(keyPair.getPrivate());

            KeyPairInfo info = new KeyPairInfo();
            info.setPublicKey(publicKeyPem);
            info.setPrivateKey(privateKeyPem);
            info.setAlgorithm(algorithm);
            info.setKeySize(keySize);

            log.info("用户密钥对生成成功：{} {}位", algorithm, keySize);
            return info;

        } catch (Exception e) {
            log.error("生成用户密钥对失败", e);
            throw new RuntimeException("生成用户密钥对失败：" + e.getMessage());
        }
    }

    /**
     * 将公钥编码为 PEM 格式
     */
    private String encodePublicKeyToPem(PublicKey publicKey) {
        byte[] encoded = publicKey.getEncoded();
        String base64 = Base64.getEncoder().encodeToString(encoded);

        StringBuilder pem = new StringBuilder();
        pem.append("-----BEGIN PUBLIC KEY-----\n");
        for (int i = 0; i < base64.length(); i += 64) {
            int end = Math.min(i + 64, base64.length());
            pem.append(base64.substring(i, end)).append("\n");
        }
        pem.append("-----END PUBLIC KEY-----\n");
        return pem.toString();
    }

    /**
     * 将私钥编码为 PEM 格式
     */
    private String encodePrivateKeyToPem(PrivateKey privateKey) {
        byte[] encoded = privateKey.getEncoded();
        String base64 = Base64.getEncoder().encodeToString(encoded);

        StringBuilder pem = new StringBuilder();
        pem.append("-----BEGIN PRIVATE KEY-----\n");
        for (int i = 0; i < base64.length(); i += 64) {
            int end = Math.min(i + 64, base64.length());
            pem.append(base64.substring(i, end)).append("\n");
        }
        pem.append("-----END PRIVATE KEY-----\n");
        return pem.toString();
    }

    /**
     * 密钥对信息
     */
    public static class KeyPairInfo {
        private String publicKey;
        private String privateKey;
        private String algorithm;
        private int keySize;

        public String getPublicKey() {
            return publicKey;
        }

        public void setPublicKey(String publicKey) {
            this.publicKey = publicKey;
        }

        public String getPrivateKey() {
            return privateKey;
        }

        public void setPrivateKey(String privateKey) {
            this.privateKey = privateKey;
        }

        public String getAlgorithm() {
            return algorithm;
        }

        public void setAlgorithm(String algorithm) {
            this.algorithm = algorithm;
        }

        public int getKeySize() {
            return keySize;
        }

        public void setKeySize(int keySize) {
            this.keySize = keySize;
        }
    }
}

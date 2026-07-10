package com.kuugae.ca.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Slf4j
@Component
public class KeyManager {

    private PrivateKey privateKey;
    private PublicKey publicKey;

    public void loadKeys(String keyDirectory) throws Exception {
        String privateKeyPath = keyDirectory + "/PrivateKey.pem";
        String publicKeyPath = keyDirectory + "/PublicKey.pem";

        File privateKeyFile = new File(privateKeyPath);
        File publicKeyFile = new File(publicKeyPath);

        // 如果密钥文件不存在，则生成新的密钥对
        if (!privateKeyFile.exists() || !publicKeyFile.exists()) {
            log.info("CA 密钥文件不存在，将生成新的密钥对");
            generateAndSaveKeyPair(keyDirectory);
        }

        String privateKeyContent = new String(java.nio.file.Files.readAllBytes(
                java.nio.file.Paths.get(privateKeyPath)));
        String publicKeyContent = new String(java.nio.file.Files.readAllBytes(
                java.nio.file.Paths.get(publicKeyPath)));

        this.privateKey = loadPrivateKey(privateKeyContent);
        this.publicKey = loadPublicKey(publicKeyContent);
        
        log.info("CA 密钥加载成功");
    }

    /**
     * 生成 CA 密钥对并保存到文件
     */
    private void generateAndSaveKeyPair(String keyDirectory) throws Exception {
        // 创建目录
        File dir = new File(keyDirectory);
        if (!dir.exists()) {
            boolean created = dir.mkdirs();
            if (created) {
                log.info("创建密钥目录：{}", keyDirectory);
            }
        }

        // 生成密钥对
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        
        this.privateKey = keyPair.getPrivate();
        this.publicKey = keyPair.getPublic();

        // 保存私钥
        String privateKeyPem = encodePrivateKeyToPem(this.privateKey);
        String privateKeyPath = keyDirectory + "/PrivateKey.pem";
        saveToFile(privateKeyPath, privateKeyPem);
        log.info("CA 私钥已保存至：{}", privateKeyPath);

        // 保存公钥
        String publicKeyPem = encodePublicKeyToPem(this.publicKey);
        String publicKeyPath = keyDirectory + "/PublicKey.pem";
        saveToFile(publicKeyPath, publicKeyPem);
        log.info("CA 公钥已保存至：{}", publicKeyPath);
    }

    /**
     * 将私钥编码为 PEM 格式
     */
    private String encodePrivateKeyToPem(PrivateKey privateKey) {
        byte[] encoded = privateKey.getEncoded();
        String base64 = Base64.getEncoder().encodeToString(encoded);
        
        StringBuilder pem = new StringBuilder();
        pem.append("-----BEGIN PRIVATE KEY-----\n");
        // 每行 64 个字符
        for (int i = 0; i < base64.length(); i += 64) {
            int end = Math.min(i + 64, base64.length());
            pem.append(base64.substring(i, end)).append("\n");
        }
        pem.append("-----END PRIVATE KEY-----\n");
        return pem.toString();
    }

    /**
     * 将公钥编码为 PEM 格式
     */
    private String encodePublicKeyToPem(PublicKey publicKey) {
        byte[] encoded = publicKey.getEncoded();
        String base64 = Base64.getEncoder().encodeToString(encoded);
        
        StringBuilder pem = new StringBuilder();
        pem.append("-----BEGIN PUBLIC KEY-----\n");
        // 每行 64 个字符
        for (int i = 0; i < base64.length(); i += 64) {
            int end = Math.min(i + 64, base64.length());
            pem.append(base64.substring(i, end)).append("\n");
        }
        pem.append("-----END PUBLIC KEY-----\n");
        return pem.toString();
    }

    /**
     * 保存内容到文件
     */
    private void saveToFile(String filePath, String content) throws IOException {
        File file = new File(filePath);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }
        
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(content.getBytes("UTF-8"));
        }
    }

    public void loadKeysFromEnvironment() throws Exception {
        String privateKeyPem = System.getenv("CA_PRIVATE_KEY");
        String publicKeyPem = System.getenv("CA_PUBLIC_KEY");

        if (privateKeyPem != null && publicKeyPem != null) {
            this.privateKey = loadPrivateKey(privateKeyPem);
            this.publicKey = loadPublicKey(publicKeyPem);
        } else {
            generateKeyPair();
        }
    }

    private PrivateKey loadPrivateKey(String privateKeyPem) throws Exception {
        String privateKeyContent = privateKeyPem
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");

        byte[] keyBytes = Base64.getDecoder().decode(privateKeyContent);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }

    private PublicKey loadPublicKey(String publicKeyPem) throws Exception {
        String publicKeyContent = publicKeyPem
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");

        byte[] keyBytes = Base64.getDecoder().decode(publicKeyContent);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }

    public void generateKeyPair() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        this.privateKey = keyPair.getPrivate();
        this.publicKey = keyPair.getPublic();
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }
}

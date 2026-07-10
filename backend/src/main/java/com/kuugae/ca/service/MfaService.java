package com.kuugae.ca.service;

import com.kuugae.ca.entity.User;
import com.kuugae.ca.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base32;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Slf4j
@Service
@RequiredArgsConstructor
public class MfaService {

    private final UserRepository userRepository;
    private static final int CODE_DIGITS = 6;
    private static final int TIME_STEP_SECONDS = 30;

    @Transactional
    public String generateMfaSecret(String email) {
        try {
            // 生成 20 字节的随机密钥
            byte[] keyBytes = new byte[20];
            java.security.SecureRandom random = new java.security.SecureRandom();
            random.nextBytes(keyBytes);
            
            org.apache.commons.codec.binary.Base32 base32 = new org.apache.commons.codec.binary.Base32();
            String secret = base32.encodeToString(keyBytes).toUpperCase();

            User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
            
            user.setMfaSecret(secret);
            user.setMfaEnabled(true);
            user.setMfaVerified(false);
            userRepository.save(user);

            log.info("为用户 {} 生成 MFA 密钥", email);
            return secret;

        } catch (Exception e) {
            throw new RuntimeException("生成 MFA 密钥失败", e);
        }
    }

    public String generateTotpUri(String email, String secret, String issuer) {
        String encodedSecret = secret.replaceAll("\\s", "");
        String account = email;
        String issuerEncoded = java.net.URLEncoder.encode(issuer);
        
        return String.format("otpauth://totp/%s:%s?secret=%s&issuer=%s&algorithm=SHA1&digits=6&period=30",
            issuerEncoded,
            java.net.URLEncoder.encode(account),
            encodedSecret,
            issuerEncoded);
    }

    public boolean verifyTotp(String email, String code) {
        try {
            User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

            if (!user.getMfaEnabled() || user.getMfaSecret() == null) {
                log.warn("用户 {} 未启用 MFA", email);
                return false;
            }

            boolean isValid = verifyTOTP(user.getMfaSecret(), code);

            if (isValid) {
                log.info("用户 {} TOTP 验证成功", email);
            } else {
                log.warn("用户 {} TOTP 验证失败", email);
            }

            return isValid;

        } catch (Exception e) {
            log.error("验证 TOTP 失败", e);
            return false;
        }
    }

    @Transactional
    public boolean verifyAndEnableMfa(String email, String code) {
        try {
            User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

            if (!user.getMfaEnabled() || user.getMfaSecret() == null) {
                log.warn("用户 {} 未初始化 MFA", email);
                return false;
            }

            boolean isValid = verifyTOTP(user.getMfaSecret(), code);

            if (isValid) {
                user.setMfaVerified(true);
                userRepository.save(user);
                log.info("用户 {} MFA 验证并启用成功", email);
                return true;
            } else {
                log.warn("用户 {} MFA 验证码错误", email);
                return false;
            }

        } catch (Exception e) {
            log.error("验证并启用 MFA 失败", e);
            return false;
        }
    }

    @Transactional
    public boolean disableMfa(String email, String code) {
        try {
            User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

            if (!user.getMfaEnabled()) {
                log.warn("用户 {} 未启用 MFA", email);
                return false;
            }

            if (!verifyTotp(email, code)) {
                log.warn("用户 {} 禁用 MFA 时验证码错误", email);
                return false;
            }

            user.setMfaEnabled(false);
            user.setMfaVerified(false);
            user.setMfaSecret(null);
            userRepository.save(user);

            log.info("用户 {} 禁用 MFA 成功", email);
            return true;

        } catch (Exception e) {
            log.error("禁用 MFA 失败", e);
            return false;
        }
    }

    public boolean isMfaEnabled(String email) {
        java.util.Optional<User> userOpt = userRepository.findByEmail(email);
        return userOpt.map(user -> user.getMfaEnabled() && user.getMfaVerified()).orElse(false);
    }

    public boolean isMfaSetupRequired(String email) {
        java.util.Optional<User> userOpt = userRepository.findByEmail(email);
        return userOpt.map(user -> user.getMfaEnabled() && !user.getMfaVerified()).orElse(false);
    }

    /**
     * 验证 TOTP 码
     */
    private boolean verifyTOTP(String secret, String code) {
        try {
            org.apache.commons.codec.binary.Base32 base32 = new org.apache.commons.codec.binary.Base32();
            byte[] key = base32.decode(secret);
            long currentTime = System.currentTimeMillis() / 1000;
            long currentIndex = currentTime / TIME_STEP_SECONDS;

            // 检查当前时间窗口和前后各一个窗口（允许 ±30 秒 误差）
            for (int i = -1; i <= 1; i++) {
                long index = currentIndex + i;
                String generatedCode = generateCode(key, index);
                if (generatedCode.equals(code)) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            log.error("验证 TOTP 异常", e);
            return false;
        }
    }

    /**
     * 生成 TOTP 码
     */
    private String generateCode(byte[] key, long index) throws NoSuchAlgorithmException, InvalidKeyException {
        // 创建 8 字节的消息
        byte[] data = ByteBuffer.allocate(8).putLong(index).array();
        
        // 反转字节序（大端）
        for (int i = 0; i < 4; i++) {
            byte temp = data[i];
            data[i] = data[7 - i];
            data[7 - i] = temp;
        }

        // 使用 HMAC-SHA1 计算
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(new SecretKeySpec(key, "HmacSHA1"));
        byte[] hash = mac.doFinal(data);

        // 动态截断
        int offset = hash[hash.length - 1] & 0x0F;
        int binary = ((hash[offset] & 0x7F) << 24) |
                    ((hash[offset + 1] & 0xFF) << 16) |
                    ((hash[offset + 2] & 0xFF) << 8) |
                    (hash[offset + 3] & 0xFF);

        // 生成 6 位数字码
        int otp = binary % (int) Math.pow(10, CODE_DIGITS);
        
        // 格式化为 6 位，不足补零
        return String.format("%0" + CODE_DIGITS + "d", otp);
    }
}

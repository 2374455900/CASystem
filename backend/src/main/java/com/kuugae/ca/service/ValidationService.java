package com.kuugae.ca.service;

import com.kuugae.ca.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ValidationService {

    private static final String SIGNATURE_ALGORITHM = "SHA256withRSA";
    private static final String CIPHER_ALGORITHM = "RSA/ECB/PKCS1Padding";

    public static class CaptchaValidationResult {
        private final boolean valid;
        private final String errorMessage;

        public CaptchaValidationResult(boolean valid, String errorMessage) {
            this.valid = valid;
            this.errorMessage = errorMessage;
        }

        public boolean isValid() {
            return valid;
        }

        public String getErrorMessage() {
            return errorMessage;
        }
    }

    public static class SignatureValidationResult {
        private final boolean valid;
        private final String errorMessage;
        private final Map<String, String> decryptedParameters;

        public SignatureValidationResult(boolean valid, String errorMessage, Map<String, String> decryptedParameters) {
            this.valid = valid;
            this.errorMessage = errorMessage;
            this.decryptedParameters = decryptedParameters;
        }

        public boolean isValid() {
            return valid;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public Map<String, String> getDecryptedParameters() {
            return decryptedParameters;
        }
    }

    public CaptchaValidationResult validateCaptcha(String inputCode, String sessionCode) {
        if (inputCode == null || sessionCode == null) {
            return new CaptchaValidationResult(false, "验证码不能为空");
        }

        if (!inputCode.equalsIgnoreCase(sessionCode)) {
            return new CaptchaValidationResult(false, "验证码错误");
        }

        return new CaptchaValidationResult(true, null);
    }

    public SignatureValidationResult decryptAndValidateSignature(
            Map<String, String> parameters, PrivateKey privateKey) {
        Map<String, String> decryptedParams = new HashMap<>();

        try {
            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                String key = entry.getKey();
                String encryptedValue = entry.getValue();

                if (key.endsWith("_encrypted")) {
                    String originalKey = key.replace("_encrypted", "");
                    String decryptedValue = decrypt(encryptedValue, privateKey);
                    decryptedParams.put(originalKey, decryptedValue);
                } else if (!key.equals("check_code")) {
                    decryptedParams.put(key, encryptedValue);
                }
            }

            return new SignatureValidationResult(true, null, decryptedParams);

        } catch (Exception e) {
            return new SignatureValidationResult(false, "参数解密验证失败", null);
        }
    }

    private String decrypt(String data, PrivateKey privateKey) throws Exception {
        byte[] encryptedData = Base64.getDecoder().decode(data);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decrypted = cipher.doFinal(encryptedData);
        return new String(decrypted, StandardCharsets.UTF_8);
    }

    public String encrypt(String data, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encrypted = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encrypted);
    }

    public String sign(String data, PrivateKey privateKey) throws Exception {
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(privateKey);
        signature.update(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(signature.sign());
    }

    public boolean verify(String data, String signatureStr, PublicKey publicKey) throws Exception {
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(publicKey);
        signature.update(data.getBytes(StandardCharsets.UTF_8));
        return signature.verify(Base64.getDecoder().decode(signatureStr));
    }
}

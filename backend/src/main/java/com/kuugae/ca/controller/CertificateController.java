package com.kuugae.ca.controller;

import com.kuugae.ca.annotation.AuditOperation;
import com.kuugae.ca.dto.ApiResponse;
import com.kuugae.ca.dto.CertificateApplyRequest;
import com.kuugae.ca.dto.CertificateResponse;
import com.kuugae.ca.dto.CertificateRevokeRequest;
import com.kuugae.ca.entity.Certificate;
import com.kuugae.ca.entity.User;
import com.kuugae.ca.exception.BusinessException;
import com.kuugae.ca.repository.CertificateRepository;
import com.kuugae.ca.security.UserPrincipal;
import com.kuugae.ca.service.CertificateManagementService;
import com.kuugae.ca.service.UserService;
import com.kuugae.ca.service.SignatureGenerationService;
import com.kuugae.ca.service.UserSignatureService;
import com.kuugae.ca.service.ValidationService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/certificates")
@RequiredArgsConstructor
public class CertificateController {

    private final CertificateManagementService certificateManagementService;
    private final UserService userService;
    private final ValidationService validationService;
    private final HttpSession httpSession;
    private final UserSignatureService userSignatureService;
    private final SignatureGenerationService signatureGenerationService;
    private final CertificateRepository certificateRepository;

    @PostMapping("/apply")
    @AuditOperation(type = "证书管理", targetType = "证书申请", targetId = "#request.organization")
    public ResponseEntity<ApiResponse<Map<String, Object>>> applyCertificate(
            @Valid @RequestBody CertificateApplyRequest request) {

        String username = getCurrentUsername();

        String sessionCode = (String) getSessionAttribute("checkCode_session");
        ValidationService.CaptchaValidationResult captchaResult =
                validationService.validateCaptcha(request.getCheckCode(), sessionCode);

        if (!captchaResult.isValid()) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, captchaResult.getErrorMessage()));
        }

        Certificate certificate = certificateManagementService.applyCertificate(request, username);

        Map<String, Object> result = new HashMap<>();
        result.put("serialNumber", certificate.getSerialNumber());
        result.put("organization", certificate.getOrganization());
        result.put("chargePerson", certificate.getChargePerson());
        result.put("validTime", new String[]{certificate.getStartTime(), certificate.getEndTime()});

        removeSessionAttribute("checkCode_session");

        return ResponseEntity.ok(ApiResponse.success("证书申请成功", result));
    }

    @PostMapping("/revoke")
    @AuditOperation(type = "证书管理", targetType = "证书撤销", targetId = "#request.serialNumber")
    public ResponseEntity<ApiResponse<Map<String, Object>>> revokeCertificate(
            @Valid @RequestBody CertificateRevokeRequest request) {

        String username = getCurrentUsername();

        String sessionCode = (String) getSessionAttribute("checkCode_session");
        ValidationService.CaptchaValidationResult captchaResult =
                validationService.validateCaptcha(request.getCheckCode(), sessionCode);

        if (!captchaResult.isValid()) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, captchaResult.getErrorMessage()));
        }

        String[] revokeResult = certificateManagementService.revokeCertificate(
                request.getSerialNumber(),
                request.getRevokePassword(),
                username
        );

        Map<String, Object> result = new HashMap<>();
        result.put("serialNumber", revokeResult[0]);
        result.put("organization", revokeResult[1]);

        removeSessionAttribute("checkCode_session");

        return ResponseEntity.ok(ApiResponse.success("证书撤销成功", result));
    }

    @GetMapping("/my")
    @AuditOperation(type = "证书管理", targetType = "证书查询", targetId = "my-certificates")
    public ResponseEntity<ApiResponse<List<CertificateResponse>>> getMyCertificates() {
        String username = getCurrentUsername();
        List<CertificateResponse> certificates =
                certificateManagementService.getUserCertificates(username);

        return ResponseEntity.ok(ApiResponse.success(certificates));
    }

    @GetMapping("/{serialNumber}")
    public ResponseEntity<ApiResponse<CertificateResponse>> getCertificate(
            @PathVariable String serialNumber) {

        String filePath = certificateManagementService.getFilePath(serialNumber);
        if (filePath == null) {
            return ResponseEntity.notFound().build();
        }

        List<CertificateResponse> certificates =
                certificateManagementService.getUserCertificates(getCurrentUsername());

        CertificateResponse certificate = certificates.stream()
                .filter(c -> c.getSerialNumber().equals(serialNumber))
                .findFirst()
                .orElse(null);

        if (certificate == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(ApiResponse.success(certificate));
    }

    @GetMapping("/download/{serialNumber}")
    @AuditOperation(type = "证书管理", targetType = "证书下载", targetId = "#serialNumber")
    public ResponseEntity<ApiResponse<Map<String, String>>> getDownloadInfo(
            @PathVariable String serialNumber) {

        String filePath = certificateManagementService.getFilePath(serialNumber);

        if (filePath == null) {
            return ResponseEntity.notFound().build();
        }

        Map<String, String> downloadInfo = new HashMap<>();
        downloadInfo.put("serialNumber", serialNumber);
        downloadInfo.put("filePath", filePath);

        return ResponseEntity.ok(ApiResponse.success(downloadInfo));
    }

    @GetMapping("/download-file/{serialNumber}")
    @AuditOperation(type = "证书管理", targetType = "证书文件下载", targetId = "#serialNumber")
    public ResponseEntity<byte[]> downloadCertificateFile(
            @PathVariable String serialNumber) {

        byte[] fileContent = certificateManagementService.getCertificateFileContent(serialNumber);

        if (fileContent == null) {
            return ResponseEntity.notFound().build();
        }

        String filename = "certificate_" + serialNumber + ".cer";

        return ResponseEntity.ok()
                .header("Content-Type", "application/pkix-cert")
                .header("Content-Disposition", "attachment; filename=\"" + filename + "\"")
                .body(fileContent);
    }

    @PostMapping("/verify")
    @AuditOperation(type = "证书管理", targetType = "证书验证", targetId = "verify")
    public ResponseEntity<ApiResponse<Map<String, Object>>> verifyCertificate(
            @RequestParam("file") org.springframework.web.multipart.MultipartFile certFile) {
        try {
            byte[] certBytes = certFile.getBytes();
            
            // 使用 CA 公钥验证证书签名
            boolean isValid = certificateManagementService.verifyCertificateSignature(certBytes);
            
            // 解析证书信息
            java.security.cert.CertificateFactory cf = java.security.cert.CertificateFactory.getInstance("X.509");
            java.security.cert.X509Certificate x509Cert = (java.security.cert.X509Certificate) cf
                .generateCertificate(new java.io.ByteArrayInputStream(certBytes));
            
            Map<String, Object> result = new HashMap<>();
            result.put("valid", isValid);
            result.put("message", isValid ? "证书验证通过，由本 CA 签发" : "验证失败，证书不是由本 CA 签发或签名已损坏");
            result.put("serialNumber", x509Cert.getSerialNumber().toString(16).toUpperCase());
            result.put("subject", x509Cert.getSubjectX500Principal().getName());
            result.put("issuer", x509Cert.getIssuerX500Principal().getName());
            result.put("notBefore", x509Cert.getNotBefore());
            result.put("notAfter", x509Cert.getNotAfter());
            
            // 检查有效期
            java.util.Date now = new java.util.Date();
            boolean isExpired = x509Cert.getNotAfter().before(now);
            boolean isNotYetValid = x509Cert.getNotBefore().after(now);
            result.put("expired", isExpired);
            result.put("notYetValid", isNotYetValid);
            result.put("status", isExpired ? "已过期" : (isNotYetValid ? "尚未生效" : "有效"));
            
            return ResponseEntity.ok(ApiResponse.success("证书验证完成", result));
            
        } catch (Exception e) {
            log.error("证书验证失败", e);
            return ResponseEntity.badRequest()
                .body(ApiResponse.error(400, "证书验证失败：" + e.getMessage()));
        }
    }

    @GetMapping("/public-key")
    public ResponseEntity<ApiResponse<Map<String, String>>> getCAPublicKey() {
        try {
            String publicKeyPem = certificateManagementService.getCAPublicKeyInPem();
            
            Map<String, String> result = new HashMap<>();
            result.put("publicKey", publicKeyPem);
            result.put("algorithm", "RSA");
            result.put("keySize", "2048");
            result.put("usage", "用于验证由本 CA 签发的证书签名");
            
            return ResponseEntity.ok(ApiResponse.success(result));
            
        } catch (Exception e) {
            log.error("获取 CA 公钥失败", e);
            return ResponseEntity.internalServerError()
                .body(ApiResponse.error(500, "获取 CA 公钥失败：" + e.getMessage()));
        }
    }

    @GetMapping("/download-private-key/{serialNumber}")
    @AuditOperation(type = "证书管理", targetType = "私钥下载", targetId = "#serialNumber")
    public ResponseEntity<ApiResponse<Map<String, String>>> downloadUserPrivateKey(
            @PathVariable String serialNumber,
            Authentication authentication) {
        try {
            String currentUsername = getCurrentUsername();
            
            // 验证证书所有权
            Certificate certificate = certificateRepository.findBySerialNumber(serialNumber)
                .orElseThrow(() -> new BusinessException(404, "证书不存在"));
            
            if (!certificate.getUsername().equals(currentUsername)) {
                throw new BusinessException(403, "无权访问该证书的私钥");
            }
            
            String userPrivateKey = certificate.getUserPrivateKeyEncrypted();
            if (userPrivateKey == null || userPrivateKey.isEmpty()) {
                throw new BusinessException(404, "该证书没有关联的用户私钥");
            }
            
            Map<String, String> result = new HashMap<>();
            result.put("serialNumber", serialNumber);
            result.put("privateKey", userPrivateKey);
            result.put("algorithm", certificate.getKeyAlgorithm());
            result.put("keySize", String.valueOf(certificate.getKeySize()));
            result.put("warning", "重要：请妥善保管私钥，勿泄露给他人");
            
            return ResponseEntity.ok(ApiResponse.success(result));
            
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("下载用户私钥失败", e);
            return ResponseEntity.internalServerError()
                .body(ApiResponse.error(500, "下载用户私钥失败：" + e.getMessage()));
        }
    }

    @PostMapping("/generate-signature")
    @AuditOperation(type = "证书管理", targetType = "签名生成", targetId = "#request.serialNumber")
    public ResponseEntity<ApiResponse<Map<String, Object>>> generateSignature(
            @Valid @RequestBody com.kuugae.ca.dto.SignatureGenerationRequest request) {
        try {
            String username = getCurrentUsername();
            
            SignatureGenerationService.SignatureResult result = 
                signatureGenerationService.generateSignature(
                    request.getData(), 
                    request.getSerialNumber(), 
                    username
                );
            
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("success", result.isSuccess());
            responseData.put("signature", result.getSignature());
            responseData.put("data", result.getData());
            responseData.put("dataHash", result.getDataHash());
            responseData.put("serialNumber", result.getSerialNumber());
            responseData.put("subject", result.getSubject());
            responseData.put("algorithm", result.getAlgorithm());
            
            return ResponseEntity.ok(ApiResponse.success(result.getMessage(), responseData));
            
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("生成签名失败", e);
            return ResponseEntity.badRequest()
                .body(ApiResponse.error(400, "签名生成失败：" + e.getMessage()));
        }
    }

    @PostMapping("/verify-signature")
    @AuditOperation(type = "证书管理", targetType = "签名验证", targetId = "verify-signature")
    public ResponseEntity<ApiResponse<Map<String, Object>>> verifyUserSignature(
            @RequestParam("data") String data,
            @RequestParam("signature") String signatureBase64,
            @RequestParam("certificateFile") org.springframework.web.multipart.MultipartFile certFile) {
        try {
            // 解析证书获取用户公钥
            byte[] certBytes = certFile.getBytes();
            java.security.cert.CertificateFactory cf = java.security.cert.CertificateFactory.getInstance("X.509");
            java.security.cert.X509Certificate x509Cert = (java.security.cert.X509Certificate) 
                cf.generateCertificate(new java.io.ByteArrayInputStream(certBytes));
            
            // 获取用户公钥
            String userPublicKeyPem = "-----BEGIN PUBLIC KEY-----\n" +
                java.util.Base64.getEncoder().encodeToString(x509Cert.getPublicKey().getEncoded())
                    .replaceAll("(.{64})", "$1\n") +
                "-----END PUBLIC KEY-----\n";
            
            // 验证签名
            UserSignatureService.SignatureVerificationResult result = 
                userSignatureService.verifyUserSignature(data, signatureBase64, userPublicKeyPem);
            
            // 添加证书信息
            Map<String, Object> extendedResult = new HashMap<>();
            extendedResult.put("valid", result.isValid());
            extendedResult.put("message", result.getMessage());
            extendedResult.put("dataHash", result.getDataHash());
            extendedResult.put("certificateSubject", x509Cert.getSubjectX500Principal().getName());
            extendedResult.put("certificateIssuer", x509Cert.getIssuerX500Principal().getName());
            extendedResult.put("certificateSerialNumber", x509Cert.getSerialNumber().toString(16).toUpperCase());
            extendedResult.put("validFrom", x509Cert.getNotBefore().toString());
            extendedResult.put("validTo", x509Cert.getNotAfter().toString());
            extendedResult.put("signatureAlgorithm", x509Cert.getSigAlgName());
            
            if (result.isValid()) {
                return ResponseEntity.ok(ApiResponse.success("签名验证通过", extendedResult));
            } else {
                return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, result.getMessage()));
            }
            
        } catch (Exception e) {
            log.error("验证用户签名失败", e);
            return ResponseEntity.badRequest()
                .body(ApiResponse.error(400, "签名验证失败：" + e.getMessage()));
        }
    }

    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal) {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            // 返回实际的用户名（不是邮箱）
            return userPrincipal.getActualUsername();
        }
        return authentication.getName();
    }

    private Object getSessionAttribute(String key) {
        if (httpSession != null) {
            return httpSession.getAttribute(key);
        }
        return null;
    }

    private void removeSessionAttribute(String key) {
        if (httpSession != null) {
            httpSession.removeAttribute(key);
        }
    }
}

package com.kuugae.ca.service;

import com.kuugae.ca.dto.CertificateInfo;
import com.kuugae.ca.dto.ValidationResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.openssl.PEMParser;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CertificateValidationService {

    public CertificateInfo parseCertificate(byte[] certBytes) {
        try {
            X509Certificate cert = loadCertificateFromBytes(certBytes);
            
            CertificateInfo info = CertificateInfo.builder()
                .subject(cert.getSubjectX500Principal().getName())
                .issuer(cert.getIssuerX500Principal().getName())
                .serialNumber(cert.getSerialNumber().toString(16).toUpperCase())
                .notBefore(cert.getNotBefore())
                .notAfter(cert.getNotAfter())
                .signatureAlgorithm(cert.getSigAlgName())
                .version(cert.getVersion())
                .publicKeyAlgorithm(cert.getPublicKey().getAlgorithm())
                .publicKeySize(getKeySize(cert))
                .fingerprint(calculateFingerprint(cert))
                .extensions(parseExtensions(cert))
                .build();

            log.info("证书解析成功：{}", cert.getSerialNumber());
            return info;

        } catch (Exception e) {
            log.error("证书解析失败", e);
            throw new RuntimeException("证书解析失败：" + e.getMessage());
        }
    }

    public ValidationResult validateCertificateChain(byte[] certBytes, List<byte[]> chainBytes) {
        try {
            X509Certificate cert = loadCertificateFromBytes(certBytes);
            List<X509Certificate> chain = new ArrayList<>();

            for (byte[] chainCertBytes : chainBytes) {
                X509Certificate chainCert = loadCertificateFromBytes(chainCertBytes);
                chain.add(chainCert);
            }

            ValidationResult result = ValidationResult.builder()
                .valid(true)
                .message("证书链验证通过")
                .build();

            // 验证证书有效期
            Date now = new Date();
            if (cert.getNotBefore().after(now)) {
                result.setValid(false);
                result.setMessage("证书尚未生效");
                return result;
            }
            if (cert.getNotAfter().before(now)) {
                result.setValid(false);
                result.setMessage("证书已过期");
                return result;
            }

            // 验证证书链签名
            if (!chain.isEmpty()) {
                for (int i = 0; i < chain.size() - 1; i++) {
                    X509Certificate current = chain.get(i);
                    X509Certificate issuer = chain.get(i + 1);
                    
                    try {
                        current.verify(issuer.getPublicKey());
                    } catch (Exception e) {
                        result.setValid(false);
                        result.setMessage("证书链签名验证失败：" + e.getMessage());
                        return result;
                    }
                }

                // 验证终端证书与链的签名
                try {
                    cert.verify(chain.get(0).getPublicKey());
                } catch (Exception e) {
                    result.setValid(false);
                    result.setMessage("证书与颁发者签名验证失败：" + e.getMessage());
                    return result;
                }
            }

            log.info("证书链验证成功");
            return result;

        } catch (Exception e) {
            log.error("证书链验证失败", e);
            return ValidationResult.builder()
                .valid(false)
                .message("证书链验证失败：" + e.getMessage())
                .build();
        }
    }

    public CertificateInfo.CertificateStatus checkCertificateStatus(byte[] certBytes) {
        try {
            X509Certificate cert = loadCertificateFromBytes(certBytes);
            Date now = new Date();

            boolean isExpired = cert.getNotAfter().before(now);
            boolean isNotYetValid = cert.getNotBefore().after(now);
            boolean isValid = !isExpired && !isNotYetValid;

            return CertificateInfo.CertificateStatus.builder()
                .valid(isValid)
                .expired(isExpired)
                .notYetValid(isNotYetValid)
                .notBefore(cert.getNotBefore())
                .notAfter(cert.getNotAfter())
                .daysUntilExpiry(calculateDaysUntilExpiry(cert))
                .build();

        } catch (Exception e) {
            log.error("检查证书状态失败", e);
            throw new RuntimeException("检查证书状态失败：" + e.getMessage());
        }
    }

    private X509Certificate loadCertificateFromBytes(byte[] certBytes) throws CertificateException {
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            return (X509Certificate) cf.generateCertificate(new ByteArrayInputStream(certBytes));
        } catch (CertificateException e) {
            // 尝试 PEM 格式
            try {
                String pemContent = new String(certBytes);
                if (pemContent.contains("-----BEGIN CERTIFICATE-----")) {
                    PEMParser pemParser = new PEMParser(new StringReader(pemContent));
                    X509CertificateHolder certHolder = (X509CertificateHolder) pemParser.readObject();
                    return new JcaX509CertificateConverter().getCertificate(certHolder);
                }
            } catch (Exception ex) {
                throw e;
            }
            throw e;
        }
    }

    private int getKeySize(X509Certificate cert) {
        try {
            String alg = cert.getPublicKey().getAlgorithm();
            if ("RSA".equals(alg) || "EC".equals(alg)) {
                return cert.getPublicKey().getEncoded().length * 8;
            }
        } catch (Exception e) {
            // 忽略
        }
        return -1;
    }

    private String calculateFingerprint(X509Certificate cert) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(cert.getEncoded());
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02X:", b));
            }
            if (sb.length() > 0) {
                sb.setLength(sb.length() - 1);
            }
            return sb.toString();
        } catch (Exception e) {
            return "N/A";
        }
    }

    private List<CertificateInfo.ExtensionInfo> parseExtensions(X509Certificate cert) {
        List<CertificateInfo.ExtensionInfo> extensions = new ArrayList<>();

        // 解析基本约束
        int basicConstraints = cert.getBasicConstraints();
        if (basicConstraints >= 0) {
            extensions.add(CertificateInfo.ExtensionInfo.builder()
                .oid("2.5.29.19")
                .name("基本约束")
                .value("CA: " + (basicConstraints >= 0 ? "是" : "否"))
                .critical(true)
                .build());
        }

        // 解析密钥用途
        boolean[] keyUsage = cert.getKeyUsage();
        if (keyUsage != null) {
            StringBuilder usage = new StringBuilder();
            String[] usageNames = {"数字签名", "不可抵赖", "密钥加密", "数据加密", 
                                  "密钥协商", "证书签名", "CRL 签名", "加密", "解密"};
            for (int i = 0; i < keyUsage.length; i++) {
                if (keyUsage[i]) {
                    if (usage.length() > 0) usage.append(", ");
                    usage.append(usageNames[i]);
                }
            }
            extensions.add(CertificateInfo.ExtensionInfo.builder()
                .oid("2.5.29.15")
                .name("密钥用途")
                .value(usage.toString())
                .critical(true)
                .build());
        }

        // 解析增强密钥用途
        try {
            java.util.List<String> extKeyUsage = cert.getExtendedKeyUsage();
            if (extKeyUsage != null && !extKeyUsage.isEmpty()) {
                extensions.add(CertificateInfo.ExtensionInfo.builder()
                    .oid("2.5.29.37")
                    .name("增强密钥用途")
                    .value(String.join(", ", extKeyUsage))
                    .critical(false)
                    .build());
            }
        } catch (Exception e) {
            // 忽略
        }

        // 解析主题备用名称
        try {
            java.util.Collection<List<?>> san = (java.util.Collection<List<?>>) 
                cert.getSubjectAlternativeNames();
            if (san != null) {
                StringBuilder sanStr = new StringBuilder();
                for (List<?> entry : san) {
                    if (sanStr.length() > 0) sanStr.append(", ");
                    sanStr.append(entry.get(1));
                }
                extensions.add(CertificateInfo.ExtensionInfo.builder()
                    .oid("2.5.29.17")
                    .name("主题备用名称")
                    .value(sanStr.toString())
                    .critical(false)
                    .build());
            }
        } catch (Exception e) {
            // 忽略
        }

        return extensions;
    }

    private long calculateDaysUntilExpiry(X509Certificate cert) {
        Date now = new Date();
        long diff = cert.getNotAfter().getTime() - now.getTime();
        return diff / (1000 * 60 * 60 * 24);
    }
}

package com.kuugae.ca.service;

import com.kuugae.ca.exception.BusinessException;
import com.kuugae.ca.utils.CertificateGenerator;
import com.kuugae.ca.utils.KeyManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CertificateService {

    @Value("${app.certificate.download-path}")
    private String downloadPath;

    @Value("${app.certificate.key-directory}")
    private String keyDirectory;

    private final KeyManager keyManager;

    public static class CertificateGenerationResult {
        private final X509Certificate certificate;
        private final String serialNumber;
        private final String filePath;
        private final String[] validTime;
        private final String errorMessage;

        public CertificateGenerationResult(X509Certificate certificate, String serialNumber,
                                          String filePath, String[] validTime) {
            this.certificate = certificate;
            this.serialNumber = serialNumber;
            this.filePath = filePath;
            this.validTime = validTime;
            this.errorMessage = null;
        }

        public CertificateGenerationResult(String errorMessage) {
            this.certificate = null;
            this.serialNumber = null;
            this.filePath = null;
            this.validTime = null;
            this.errorMessage = errorMessage;
        }

        public boolean isSuccess() {
            return certificate != null;
        }

        public X509Certificate getCertificate() {
            return certificate;
        }

        public String getSerialNumber() {
            return serialNumber;
        }

        public String getFilePath() {
            return filePath;
        }

        public String[] getValidTime() {
            return validTime;
        }

        public String getErrorMessage() {
            return errorMessage;
        }
    }

    public CertificateGenerationResult generateAndSaveCertificate(
            PublicKey publicKey, String organization, String juridicalPerson, int validityDays) {

        try {
            PrivateKey caPrivateKey = loadCAPrivateKey();

            String serialNumber = generateSerialNumber();
            String[] validTime = calculateValidTime(validityDays);
            HashMap<String, String> certInfo = buildCertificateInfo(
                    serialNumber, organization, juridicalPerson, validTime);

            X509Certificate certificate = CertificateGenerator.generateCertificate(
                    publicKey, caPrivateKey, certInfo);

            if (certificate == null) {
                return new CertificateGenerationResult("证书生成失败");
            }

            String filePath = saveCertificateFile(certificate, serialNumber);

            return new CertificateGenerationResult(certificate, serialNumber, filePath, validTime);

        } catch (Exception e) {
            return new CertificateGenerationResult("证书生成异常：" + e.getMessage());
        }
    }

    private PrivateKey loadCAPrivateKey() throws Exception {
        keyManager.loadKeys(keyDirectory);
        return keyManager.getPrivateKey();
    }

    private String generateSerialNumber() {
        return String.valueOf(System.currentTimeMillis());
    }

    private String[] calculateValidTime(int days) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy 年 MM 月 dd 日 HH:mm:ss");
        Calendar calendar = Calendar.getInstance();

        String startTime = sdf.format(calendar.getTime());
        calendar.add(Calendar.DAY_OF_YEAR, days);
        String endTime = sdf.format(calendar.getTime());

        return new String[]{startTime, endTime};
    }

    private HashMap<String, String> buildCertificateInfo(String serialNumber, String organization,
                                                         String juridicalPerson, String[] validTime) {
        HashMap<String, String> certInfo = new HashMap<>();

        certInfo.put("domainName", "www_santianqiku_cn");
        certInfo.put("orgUnitName", organization);
        certInfo.put("orgName", juridicalPerson);
        certInfo.put("countryCode", "CN");
        certInfo.put("city", "太原市");
        certInfo.put("province", "山西省");
        certInfo.put("validStartTime", validTime[0]);
        certInfo.put("validEndTime", validTime[1]);
        certInfo.put("serialNumber", String.valueOf(new Date().getTime() / 1000L));
        certInfo.put("signatureAlgorithm", "SHA256withRSA");
        certInfo.put("encryptType", "RSA");
        certInfo.put("encryptNum", "1024");
        certInfo.put("provider", "BC");

        return certInfo;
    }

    private String saveCertificateFile(X509Certificate certificate, String serialNumber)
            throws IOException, CertificateEncodingException {
        File dir = new File(downloadPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String fileName = serialNumber + ".cer";
        String filePath = downloadPath + File.separator + fileName;

        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(certificate.getEncoded());
        }

        return filePath;
    }

    public boolean isOrganizationApplied(String organization) {
        long certCount = 0;
        long crlCount = 0;

        return certCount == crlCount + 1;
    }

    public boolean isRegistrationNumberApplied(String registrationNumber) {
        long certCount = 0;
        long crlCount = 0;

        return certCount == crlCount + 1;
    }
}

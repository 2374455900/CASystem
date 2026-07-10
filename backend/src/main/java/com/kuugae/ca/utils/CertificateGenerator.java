package com.kuugae.ca.utils;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;

public class CertificateGenerator {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static X509Certificate generateCertificate(
            PublicKey userPublicKey,
            PrivateKey caPrivateKey,
            HashMap<String, String> certInfo) {

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy 年 MM 月 dd 日 HH:mm:ss", java.util.Locale.CHINA);
            sdf.setLenient(false);

            Date notBefore = sdf.parse(certInfo.get("validStartTime"));
            Date notAfter = sdf.parse(certInfo.get("validEndTime"));

            BigInteger serialNumber = new BigInteger(certInfo.get("serialNumber"));

            String issuerDN = "CN=" + certInfo.get("city") + ", ST=" + certInfo.get("province")
                    + ", O=" + certInfo.get("orgUnitName") + ", OU=" + certInfo.get("orgName")
                    + ", C=" + certInfo.get("countryCode");

            String subjectDN = "CN=CA System, OU=" + certInfo.get("orgUnitName")
                    + ", O=" + certInfo.get("orgName") + ", C=" + certInfo.get("countryCode");

            X500Name issuer = new X500Name(issuerDN);
            X500Name subject = new X500Name(subjectDN);

            X509v3CertificateBuilder certBuilder = new JcaX509v3CertificateBuilder(
                    issuer,
                    serialNumber,
                    notBefore,
                    notAfter,
                    subject,
                    userPublicKey);

            String signatureAlgorithm = certInfo.getOrDefault("signatureAlgorithm", "SHA256withRSA");
            ContentSigner contentSigner = new JcaContentSignerBuilder(signatureAlgorithm)
                    .setProvider("BC")
                    .build(caPrivateKey);

            return new JcaX509CertificateConverter()
                    .setProvider("BC")
                    .getCertificate(certBuilder.build(contentSigner));

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String readKeyFile(String filePath) throws IOException {
        try (FileInputStream fis = new FileInputStream(filePath);
             java.io.BufferedReader reader = new java.io.BufferedReader(
                     new java.io.InputStreamReader(fis))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            return content.toString();
        }
    }

    public static PrivateKey getPrivateKeyFromString(String privateKeyContent) throws Exception {
        String keyContent = privateKeyContent
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");

        byte[] keyBytes = Base64.getDecoder().decode(keyContent);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }

    public static PublicKey getPublicKeyFromString(String publicKeyContent) throws Exception {
        String keyContent = publicKeyContent
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");

        byte[] keyBytes = Base64.getDecoder().decode(keyContent);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }

    public static String saveCertificateToFile(X509Certificate certificate, String filePath)
            throws IOException, CertificateEncodingException {
        File file = new File(filePath);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(certificate.getEncoded());
        }

        return filePath;
    }

    public static X509Certificate loadCertificateFromFile(String filePath) throws Exception {
        try (FileInputStream fis = new FileInputStream(filePath)) {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            return (X509Certificate) cf.generateCertificate(fis);
        }
    }
}

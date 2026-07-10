package com.kuugae.ca.service;

import com.kuugae.ca.dto.CertificateApplyRequest;
import com.kuugae.ca.dto.CertificateResponse;
import com.kuugae.ca.entity.Certificate;
import com.kuugae.ca.entity.Crl;
import com.kuugae.ca.entity.User;
import com.kuugae.ca.exception.BusinessException;
import com.kuugae.ca.repository.CertificateRepository;
import com.kuugae.ca.repository.CrlRepository;
import com.kuugae.ca.utils.KeyManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.openssl.PEMParser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.PublicKey;
import java.util.Set;
import java.util.stream.Collectors;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CertificateManagementService {

    private final CertificateRepository certificateRepository;
    private final CrlRepository crlRepository;
    private final CertificateService certificateService;
    private final KeyManager keyManager;
    private final UserKeyService userKeyService;
    private final UserService userService;

    @Transactional
    public Certificate applyCertificate(CertificateApplyRequest request, String username) {
        long certOrgCount = certificateRepository.countByOrganization(request.getOrganization());
        long crlOrgCount = crlRepository.countByOrganization(request.getOrganization());
        if (certOrgCount == crlOrgCount + 1) {
            throw new BusinessException(400, "该组织结构已持有有效证书");
        }

        long certRegCount = certificateRepository.countByRegistrationNumber(request.getRegistrationNumber());
        long crlRegCount = crlRepository.countByRegistrationNumber(request.getRegistrationNumber());
        if (certRegCount == crlRegCount + 1) {
            throw new BusinessException(400, "该工商注册号已持有有效证书");
        }

        // 根据用户选择的算法和密钥长度生成密钥对
        String algorithm = request.getKeyAlgorithm() != null ? request.getKeyAlgorithm() : "RSA";
        int keySize = request.getKeySize() > 0 ? request.getKeySize() : 2048;
        
        // 验证密钥长度是否合法
        if ("RSA".equals(algorithm)) {
            if (keySize < 2048 || keySize > 4096) {
                throw new BusinessException(400, "RSA 密钥长度必须在 2048-4096 位之间");
            }
        } else if ("EC".equals(algorithm)) {
            if (keySize < 256 || keySize > 521) {
                throw new BusinessException(400, "EC 密钥长度必须在 256-521 位之间");
            }
        } else {
            throw new BusinessException(400, "不支持的密钥算法：" + algorithm);
        }
        
        log.info("为用户 {} 生成密钥对，算法：{}，密钥长度：{}", username, algorithm, keySize);
        
        // 生成用户密钥对
        UserKeyService.KeyPairInfo userKeyPair = userKeyService.generateUserKeyPair(algorithm, keySize);
        
        String publicKeyStr = userKeyPair.getPublicKey();
        String cleanedKey = publicKeyStr.replace("-----BEGIN PUBLIC KEY-----", "");
        cleanedKey = cleanedKey.replace("-----END PUBLIC KEY-----", "");
        cleanedKey = cleanedKey.replaceAll("\\s", "");

        byte[] keyBytes = Base64.getDecoder().decode(cleanedKey);
        java.security.spec.X509EncodedKeySpec keySpec =
                new java.security.spec.X509EncodedKeySpec(keyBytes);
        java.security.KeyFactory keyFactory;
        java.security.PublicKey publicKey;
        try {
            keyFactory = java.security.KeyFactory.getInstance(algorithm);
            publicKey = keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            log.error("公钥解析失败", e);
            throw new BusinessException(400, "公钥格式不正确：" + e.getMessage());
        }

        CertificateService.CertificateGenerationResult result =
                certificateService.generateAndSaveCertificate(
                        publicKey,
                        request.getOrganization(),
                        request.getJuridicalPerson(),
                        request.getValidityPeriod());

        if (!result.isSuccess()) {
            throw new BusinessException(500, result.getErrorMessage());
        }

        Certificate certificate = new Certificate();
        certificate.setSerialNumber(result.getSerialNumber());
        certificate.setOrganization(request.getOrganization());
        certificate.setRegistrationNumber(request.getRegistrationNumber());
        certificate.setFilePath(result.getFilePath());
        certificate.setStartTime(result.getValidTime()[0]);
        certificate.setEndTime(result.getValidTime()[1]);
        certificate.setJuridicalPerson(request.getJuridicalPerson());
        certificate.setChargePerson(request.getChargePerson());
        certificate.setChargePhone(request.getChargePhone());
        certificate.setUsername(username);
        certificate.setState("使用中");
        certificate.setCreatedAt(LocalDateTime.now());
        
        // 保存用户密钥对
        certificate.setUserPublicKey(userKeyPair.getPublicKey());
        certificate.setUserPrivateKeyEncrypted(userKeyPair.getPrivateKey());
        certificate.setKeyAlgorithm(algorithm);
        certificate.setKeySize(keySize);

        log.info("证书申请成功，序列号：{}，算法：{}，密钥长度：{}", result.getSerialNumber(), algorithm, keySize);

        return certificateRepository.save(certificate);
    }

    @Transactional
    public String[] revokeCertificate(String serialNumber, String revokePassword, String username) {
        // 1. 验证用户密码
        User user;
        try {
            user = userService.findByUsername(username);
        } catch (BusinessException e) {
            if (e.getCode() == 404) {
                // 当前登录的用户理论上一定存在，如果出现404可能是数据问题
                log.error("撤销证书 - 找不到当前登录用户: {}", username);
                throw new BusinessException(500, "系统错误：无法获取当前用户信息");
            }
            throw e;
        }
        
        // 验证密码
        if (!userService.verifyPassword(user, revokePassword)) {
            throw new BusinessException(401, "验证密码错误");
        }
        
        // 2. 查找证书
        Optional<Certificate> certOpt = certificateRepository.findBySerialNumber(serialNumber);
        if (certOpt.isEmpty()) {
            throw new BusinessException(404, "证书不存在或已失效");
        }

        Certificate certificate = certOpt.get();

        // 3. 验证证书所有权
        // 支持用户名或邮箱匹配（兼容旧证书数据）
        String certUsername = certificate.getUsername();
        User currentUser = userService.findByUsername(username);
        String currentUserEmail = currentUser.getEmail();
        
        log.info("撤销证书 - 验证所有权: 证书用户名='{}', 当前用户='{}', 当前邮箱='{}', 序列号='{}'", 
            certUsername, username, currentUserEmail, serialNumber);
            
        // 证书用户名匹配当前用户名或当前邮箱
        if (!certUsername.equals(username) && !certUsername.equals(currentUserEmail)) {
            log.warn("撤销证书 - 所有权验证失败: 证书用户名='{}' 不匹配 当前用户='{}' 或 当前邮箱='{}'", 
                certUsername, username, currentUserEmail);
            throw new BusinessException(403, "无权撤销此证书");
        }
        
        // 4. 检查证书状态
        if ("已注销".equals(certificate.getState())) {
            throw new BusinessException(400, "证书已经撤销");
        }

        // 5. 验证证书有效期
        SimpleDateFormat sdfWithSpace = new SimpleDateFormat("yyyy 年 MM 月 dd 日 HH:mm:ss");
        SimpleDateFormat sdfWithoutSpace = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        
        String startTimeStr = certificate.getStartTime();
        String endTimeStr = certificate.getEndTime();
        log.info("撤销证书 - 序列号: {}, 开始时间: {}, 结束时间: {}", serialNumber, startTimeStr, endTimeStr);
        
        try {
            Date endDate = parseDateFlexible(endTimeStr, sdfWithSpace, sdfWithoutSpace);
            Date startDate = parseDateFlexible(startTimeStr, sdfWithSpace, sdfWithoutSpace);
            
            Date now = new Date();
            log.info("撤销证书 - 解析结果: 开始时间={}, 结束时间={}, 当前时间={}", startDate, endDate, now);

            if (endDate.before(now) || startDate.after(now)) {
                throw new BusinessException(400, "证书不在有效期内");
            }
        } catch (Exception e) {
            log.error("撤销证书 - 日期解析失败: 开始时间='{}', 结束时间='{}', 错误: {}", 
                startTimeStr, endTimeStr, e.getMessage(), e);
            throw new BusinessException(500, "日期解析错误: " + e.getMessage() + 
                ", 开始时间='" + startTimeStr + "', 结束时间='" + endTimeStr + "'");
        }

        Crl crl = new Crl();
        crl.setSerialNumber(certificate.getSerialNumber());
        crl.setOrganization(certificate.getOrganization());
        crl.setRegistrationNumber(certificate.getRegistrationNumber());
        crl.setStartTime(certificate.getStartTime());
        crl.setEndTime(certificate.getEndTime());
        crlRepository.save(crl);

        certificate.setState("已注销");
        certificateRepository.save(certificate);

        updateCrlFile();

        return new String[]{serialNumber, certificate.getOrganization()};
    }

    private void updateCrlFile() {
        List<Crl> crlList = crlRepository.findAll();
        String filePath = "./downloads/crl.xml";
        File file = new File(filePath);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("<?xml version='1.0' encoding='utf-8'?>");
            writer.newLine();
            writer.write("<crls>");
            writer.newLine();

            for (Crl crl : crlList) {
                writer.write("<crl>");
                writer.newLine();
                writer.write("<serial_number>" + crl.getSerialNumber() + "</serial_number>");
                writer.newLine();
                writer.write("<organization>" + crl.getOrganization() + "</organization>");
                writer.newLine();
                writer.write("<start_time>" + crl.getStartTime() + "</start_time>");
                writer.newLine();
                writer.write("<end_time>" + crl.getEndTime() + "</end_time>");
                writer.newLine();
                writer.write("</crl>");
                writer.newLine();
            }

            writer.write("</crls>");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 灵活解析日期字符串，支持多种格式
     */
    private Date parseDateFlexible(String dateStr, SimpleDateFormat... formats) throws ParseException {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            throw new ParseException("日期字符串为空", 0);
        }
        
        // 首先尝试提供的格式
        for (SimpleDateFormat format : formats) {
            try {
                return format.parse(dateStr);
            } catch (ParseException e) {
                // 继续尝试下一个格式
            }
        }
        
        // 尝试ISO格式 (2024-01-01T00:00:00Z)
        try {
            return Date.from(java.time.Instant.parse(dateStr));
        } catch (Exception e) {
            // 继续
        }
        
        // 尝试常见的日期格式
        String[] commonPatterns = {
            "yyyy-MM-dd HH:mm:ss",
            "yyyy/MM/dd HH:mm:ss",
            "yyyy-MM-dd",
            "yyyy/MM/dd"
        };
        
        for (String pattern : commonPatterns) {
            try {
                return new SimpleDateFormat(pattern).parse(dateStr);
            } catch (ParseException e) {
                // 继续尝试下一个
            }
        }
        
        throw new ParseException("无法解析日期: '" + dateStr + "'", 0);
    }

    public List<CertificateResponse> getUserCertificates(String username) {
        // 获取当前用户信息，用于兼容旧证书数据（旧证书可能存储的是邮箱）
        User currentUser = userService.findByUsername(username);
        String userEmail = currentUser.getEmail();
        
        log.info("查询用户证书: 用户名='{}', 邮箱='{}'", username, userEmail);
        
        // 查询用户名匹配的证书
        List<Certificate> certificates = certificateRepository.findByUsernameOrderByCreatedAtDesc(username);
        
        // 如果用户名和邮箱不同，再查询邮箱匹配的证书（兼容旧数据）
        if (!username.equals(userEmail)) {
            List<Certificate> emailCertificates = certificateRepository.findByUsernameOrderByCreatedAtDesc(userEmail);
            
            // 合并两个列表，去重
            Set<String> serialNumbers = certificates.stream()
                    .map(Certificate::getSerialNumber)
                    .collect(Collectors.toSet());
            
            for (Certificate cert : emailCertificates) {
                if (!serialNumbers.contains(cert.getSerialNumber())) {
                    certificates.add(cert);
                }
            }
            
            // 按创建时间排序
            certificates.sort((c1, c2) -> c2.getCreatedAt().compareTo(c1.getCreatedAt()));
        }
        
        log.info("查询到 {} 个证书", certificates.size());
        
        return certificates.stream()
                .map(this::convertToResponse)
                .toList();
    }

    public Page<Certificate> getCertificatesByPage(int page, int pageSize) {
        PageRequest pageRequest = PageRequest.of(page, pageSize, Sort.by("id").descending());
        return certificateRepository.findAll(pageRequest);
    }

    public CertificateResponse convertToResponse(Certificate certificate) {
        return CertificateResponse.builder()
                .serialNumber(certificate.getSerialNumber())
                .organization(certificate.getOrganization())
                .registrationNumber(certificate.getRegistrationNumber())
                .startTime(certificate.getStartTime())
                .endTime(certificate.getEndTime())
                .juridicalPerson(certificate.getJuridicalPerson())
                .chargePerson(certificate.getChargePerson())
                .chargePhone(certificate.getChargePhone())
                .state(certificate.getState())
                .filePath(certificate.getFilePath())
                .userPublicKey(certificate.getUserPublicKey())
                .build();
    }

    public String getFilePath(String serialNumber) {
        return certificateRepository.findBySerialNumber(serialNumber)
                .map(Certificate::getFilePath)
                .orElse(null);
    }

    /**
     * 获取证书文件内容
     */
    public byte[] getCertificateFileContent(String serialNumber) {
        Optional<Certificate> certOpt = certificateRepository.findBySerialNumber(serialNumber);
        if (certOpt.isEmpty()) {
            return null;
        }

        Certificate certificate = certOpt.get();
        String filePath = certificate.getFilePath();

        if (filePath == null || filePath.isEmpty()) {
            return null;
        }

        try {
            return Files.readAllBytes(Paths.get(filePath));
        } catch (IOException e) {
            log.error("读取证书文件失败: {}", filePath, e);
            return null;
        }
    }

    /**
     * 验证证书签名 - 使用 CA 公钥验证证书是否由本 CA 签发
     */
    public boolean verifyCertificateSignature(byte[] certBytes) throws Exception {
        try {
            // 加载证书
            X509Certificate cert = loadCertificate(certBytes);
            
            // 获取 CA 公钥
            PublicKey caPublicKey = keyManager.getPublicKey();
            if (caPublicKey == null) {
                log.error("CA 公钥未加载");
                return false;
            }
            
            // 验证证书签名
            cert.verify(caPublicKey);
            
            log.info("证书 {} 验证通过", cert.getSerialNumber().toString(16).toUpperCase());
            return true;
            
        } catch (Exception e) {
            log.error("证书验证失败：{}", e.getMessage());
            return false;
        }
    }

    /**
     * 获取 CA 公钥的 PEM 格式字符串
     */
    public String getCAPublicKeyInPem() throws IOException {
        PublicKey publicKey = keyManager.getPublicKey();
        if (publicKey == null) {
            // 如果内存中没有，尝试从文件读取
            String keyDirectory = System.getenv("KEY_DIRECTORY");
            if (keyDirectory == null) {
                keyDirectory = "C:/keys";
            }
            String publicKeyPath = keyDirectory + "/PublicKey.pem";
            return new String(Files.readAllBytes(Paths.get(publicKeyPath)));
        }
        
        // 将公钥转换为 PEM 格式
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
     * 从字节数组加载 X.509 证书
     */
    private X509Certificate loadCertificate(byte[] certBytes) throws Exception {
        // 尝试直接加载 DER 格式
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            return (X509Certificate) cf.generateCertificate(new java.io.ByteArrayInputStream(certBytes));
        } catch (Exception e) {
            // 如果是 PEM 格式，先转换
            String pemContent = new String(certBytes);
            if (pemContent.contains("-----BEGIN CERTIFICATE-----")) {
                PEMParser pemParser = new PEMParser(new StringReader(pemContent));
                X509CertificateHolder certHolder = (X509CertificateHolder) pemParser.readObject();
                return new JcaX509CertificateConverter().getCertificate(certHolder);
            }
            throw e;
        }
    }
}

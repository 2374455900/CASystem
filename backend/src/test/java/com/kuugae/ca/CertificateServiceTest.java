package com.kuugae.ca;

import com.kuugae.ca.dto.CertificateApplyRequest;
import com.kuugae.ca.entity.Certificate;
import com.kuugae.ca.repository.CertificateRepository;
import com.kuugae.ca.repository.CrlRepository;
import com.kuugae.ca.service.CertificateManagementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CertificateServiceTest {

    @Autowired
    private CertificateManagementService certificateManagementService;

    @Autowired
    private CertificateRepository certificateRepository;

    @Autowired
    private CrlRepository crlRepository;

    private String testUsername;

    @BeforeEach
    void setUp() {
        certificateRepository.deleteAll();
        crlRepository.deleteAll();
        testUsername = "testuser";
    }

    @Test
    void testApplyCertificate() {
        CertificateApplyRequest request = new CertificateApplyRequest();
        request.setOrganization("Test Organization");
        request.setRegistrationNumber("123456789");
        request.setJuridicalPerson("John Doe");
        request.setChargePerson("Jane Doe");
        request.setChargePhone("13800138000");
        request.setValidityPeriod(365);
        request.setKeyAlgorithm("RSA");
        request.setKeySize(2048);
        request.setCheckCode("1234");

        Certificate certificate = certificateManagementService.applyCertificate(request, testUsername);

        assertNotNull(certificate);
        assertNotNull(certificate.getSerialNumber());
        assertEquals("Test Organization", certificate.getOrganization());
        assertEquals(testUsername, certificate.getUsername());
        assertEquals("使用中", certificate.getState());
    }

    @Test
    void testRevokeCertificate() {
        CertificateApplyRequest request = new CertificateApplyRequest();
        request.setOrganization("Test Org");
        request.setRegistrationNumber("123456789");
        request.setJuridicalPerson("John Doe");
        request.setChargePerson("Jane Doe");
        request.setChargePhone("13800138000");
        request.setValidityPeriod(365);
        request.setKeyAlgorithm("RSA");
        request.setKeySize(2048);
        request.setCheckCode("1234");

        Certificate certificate = certificateManagementService.applyCertificate(request, testUsername);

        String[] result = certificateManagementService.revokeCertificate(
                certificate.getSerialNumber(),
                "password",
                testUsername
        );

        assertNotNull(result);
        assertEquals(2, result.length);
    }

    @Test
    void testGetUserCertificates() {
        CertificateApplyRequest request = new CertificateApplyRequest();
        request.setOrganization("Test Organization");
        request.setRegistrationNumber("123456789");
        request.setJuridicalPerson("John Doe");
        request.setChargePerson("Jane Doe");
        request.setChargePhone("13800138000");
        request.setValidityPeriod(365);
        request.setKeyAlgorithm("RSA");
        request.setKeySize(2048);
        request.setCheckCode("1234");

        certificateManagementService.applyCertificate(request, testUsername);

        var certificates = certificateManagementService.getUserCertificates(testUsername);

        assertNotNull(certificates);
        assertFalse(certificates.isEmpty());
    }
}

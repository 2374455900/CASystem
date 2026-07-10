package com.kuugae.ca.dto;

import lombok.Builder;
import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
@Builder
public class CertificateInfo {
    private String subject;
    private String issuer;
    private String serialNumber;
    private Date notBefore;
    private Date notAfter;
    private String signatureAlgorithm;
    private Integer version;
    private String publicKeyAlgorithm;
    private Integer publicKeySize;
    private String fingerprint;
    private List<ExtensionInfo> extensions;

    @Data
    @Builder
    public static class ExtensionInfo {
        private String oid;
        private String name;
        private String value;
        private Boolean critical;
    }

    @Data
    @Builder
    public static class CertificateStatus {
        private Boolean valid;
        private Boolean expired;
        private Boolean notYetValid;
        private Date notBefore;
        private Date notAfter;
        private Long daysUntilExpiry;
    }
}

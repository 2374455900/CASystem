package com.kuugae.ca.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CertificateResponse {
    private String serialNumber;
    private String organization;
    private String registrationNumber;
    private String startTime;
    private String endTime;
    private String juridicalPerson;
    private String chargePerson;
    private String chargePhone;
    private String state;
    private String filePath;
    private String userPublicKey;
}

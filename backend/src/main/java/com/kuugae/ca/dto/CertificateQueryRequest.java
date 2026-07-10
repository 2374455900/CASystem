package com.kuugae.ca.dto;

import lombok.Data;

@Data
public class CertificateQueryRequest {
    private String username;
    private String organization;
    private String state;
    private String startTime;
    private String endTime;
    private Integer page = 1;
    private Integer pageSize = 10;
}

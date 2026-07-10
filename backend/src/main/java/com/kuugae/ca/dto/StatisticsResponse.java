package com.kuugae.ca.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatisticsResponse {
    private Long totalUsers;
    private Long totalCertificates;
    private Long activeCertificates;
    private Long revokedCertificates;
    private Long todayNewUsers;
    private Long todayNewCertificates;
    private List<Map<String, Object>> certificatesByState;
    private List<Map<String, Object>> usersByDate;
    private List<Map<String, Object>> certificatesByDate;
}

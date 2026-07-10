package com.kuugae.ca.service;

import com.kuugae.ca.entity.AuditLog;
import com.kuugae.ca.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public void logOperation(String operator, String operationType, String targetType,
                            String targetId, String ipAddress, String userAgent,
                            String result, String details) {
        try {
            AuditLog auditLog = new AuditLog(
                operator,
                operationType,
                targetType,
                targetId,
                ipAddress,
                userAgent,
                result,
                details
            );
            auditLogRepository.save(auditLog);
            log.debug("审计日志记录：{} - {} - {}", operator, operationType, result);
        } catch (Exception e) {
            log.error("记录审计日志失败", e);
        }
    }

    public Page<AuditLog> getLogsByUser(String operator, int page, int size) {
        return auditLogRepository.findByOperator(operator, PageRequest.of(page, size, Sort.by("timestamp").descending()));
    }

    public Page<AuditLog> getLogsByOperation(String operationType, int page, int size) {
        return auditLogRepository.findByOperationType(operationType, PageRequest.of(page, size, Sort.by("timestamp").descending()));
    }

    public Page<AuditLog> getLogsByDateRange(LocalDateTime start, LocalDateTime end, int page, int size) {
        return auditLogRepository.findByTimestampBetween(start, end, PageRequest.of(page, size, Sort.by("timestamp").descending()));
    }

    public Page<AuditLog> getLogsByTarget(String targetType, String targetId, int page, int size) {
        return auditLogRepository.findByTarget(targetType, targetId, PageRequest.of(page, size, Sort.by("timestamp").descending()));
    }

    public Page<AuditLog> getAllLogs(int page, int size) {
        return auditLogRepository.findAll(PageRequest.of(page, size, Sort.by("timestamp").descending()));
    }
}

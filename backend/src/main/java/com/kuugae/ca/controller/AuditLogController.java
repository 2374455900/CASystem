package com.kuugae.ca.controller;

import com.kuugae.ca.dto.ApiResponse;
import com.kuugae.ca.dto.PageResponse;
import com.kuugae.ca.entity.AuditLog;
import com.kuugae.ca.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/audit-logs")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AuditLogController {

    private final AuditLogService auditLogService;

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<AuditLog>>> getAuditLogs(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String operationType,
            @RequestParam(required = false) String result) {
        
        Page<AuditLog> auditLogPage;
        
        if (operationType != null && !operationType.isEmpty()) {
            auditLogPage = auditLogService.getLogsByOperation(operationType, page - 1, pageSize);
        } else if (result != null && !result.isEmpty()) {
            // 需要根据结果筛选，可以在 Service 中添加对应方法
            auditLogPage = auditLogService.getAllLogs(page - 1, pageSize);
        } else {
            auditLogPage = auditLogService.getAllLogs(page - 1, pageSize);
        }
        
        PageResponse<AuditLog> response = PageResponse.<AuditLog>builder()
                .page(page)
                .pageSize(pageSize)
                .total(auditLogPage.getTotalElements())
                .totalPages(auditLogPage.getTotalPages())
                .records(auditLogPage.getContent())
                .build();
        
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}

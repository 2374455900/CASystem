package com.kuugae.ca.controller;

import com.kuugae.ca.annotation.AuditOperation;
import com.kuugae.ca.dto.*;
import com.kuugae.ca.entity.Certificate;
import com.kuugae.ca.entity.OperationLog;
import com.kuugae.ca.entity.User;
import com.kuugae.ca.security.UserPrincipal;
import com.kuugae.ca.service.AdminService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<PageResponse<User>>> getUsers(@Valid UserQueryRequest request) {
        Page<User> page = adminService.getUsers(request);
        return ResponseEntity.ok(ApiResponse.success(convertToPageResponse(page)));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<ApiResponse<User>> getUserById(@PathVariable Integer id) {
        User user = adminService.getUserById(id);
        return ResponseEntity.ok(ApiResponse.success(user));
    }

    @DeleteMapping("/users/{id}")
    @AuditOperation(type = "用户管理", targetType = "用户", targetId = "#id")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Integer id, HttpServletRequest request) {
        UserPrincipal currentUser = getCurrentUser();
        adminService.deleteUser(id, currentUser.getId(), currentUser.getUsername());
        return ResponseEntity.ok(ApiResponse.success("用户删除成功", null));
    }

    @GetMapping("/certificates")
    @AuditOperation(type = "证书管理", targetType = "证书查询", targetId = "all")
    public ResponseEntity<ApiResponse<PageResponse<Certificate>>> getCertificates(@Valid CertificateQueryRequest request) {
        Page<Certificate> page = adminService.getCertificates(request);
        return ResponseEntity.ok(ApiResponse.success(convertToPageResponse(page)));
    }

    @GetMapping("/certificates/user/{username}")
    public ResponseEntity<ApiResponse<List<Certificate>>> getUserCertificates(@PathVariable String username) {
        List<Certificate> certificates = adminService.getUserCertificates(username);
        return ResponseEntity.ok(ApiResponse.success(certificates));
    }

    @GetMapping("/statistics")
    public ResponseEntity<ApiResponse<StatisticsResponse>> getStatistics() {
        StatisticsResponse statistics = adminService.getStatistics();
        return ResponseEntity.ok(ApiResponse.success(statistics));
    }

    @GetMapping("/logs")
    public ResponseEntity<ApiResponse<PageResponse<OperationLog>>> getOperationLogs(
            @RequestParam(required = false) Integer operatorId,
            @RequestParam(required = false) String operationType,
            @RequestParam(required = false) LocalDateTime startTime,
            @RequestParam(required = false) LocalDateTime endTime,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<OperationLog> pageResult = adminService.getOperationLogs(
                operatorId, operationType, startTime, endTime, page, pageSize);
        return ResponseEntity.ok(ApiResponse.success(convertToPageResponse(pageResult)));
    }

    private <T> PageResponse<T> convertToPageResponse(Page<T> page) {
        return PageResponse.<T>builder()
                .page(page.getNumber() + 1)
                .pageSize(page.getSize())
                .total(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .records(page.getContent())
                .build();
    }

    private UserPrincipal getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (UserPrincipal) authentication.getPrincipal();
    }
}

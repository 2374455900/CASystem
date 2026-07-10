package com.kuugae.ca.service;

import com.kuugae.ca.dto.*;
import com.kuugae.ca.entity.Certificate;
import com.kuugae.ca.entity.OperationLog;
import com.kuugae.ca.entity.User;
import com.kuugae.ca.exception.BusinessException;
import com.kuugae.ca.repository.CertificateRepository;
import com.kuugae.ca.repository.CrlRepository;
import com.kuugae.ca.repository.OperationLogRepository;
import com.kuugae.ca.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final CertificateRepository certificateRepository;
    private final CrlRepository crlRepository;
    private final OperationLogRepository operationLogRepository;

    @Transactional(readOnly = true)
    public Page<User> getUsers(UserQueryRequest request) {
        Pageable pageable = PageRequest.of(
                request.getPage() - 1,
                request.getPageSize(),
                Sort.by(Sort.Direction.fromString(request.getSortOrder()), request.getSortField())
        );

        if (request.getKeyword() != null && !request.getKeyword().isEmpty()) {
            return userRepository.findByUsernameContainingOrEmailContaining(
                    request.getKeyword(), request.getKeyword(), pageable);
        }

        return userRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public User getUserById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new BusinessException(404, "用户不存在"));
    }

    @Transactional
    public void deleteUser(Integer id, Integer operatorId, String operatorUsername) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException(404, "用户不存在"));

        userRepository.delete(user);

        saveOperationLog(operatorId, operatorUsername, "用户管理", "删除用户",
                String.format("删除用户: %s (ID: %d)", user.getUsername(), id),
                id, "USER");
    }

    @Transactional(readOnly = true)
    public Page<Certificate> getCertificates(CertificateQueryRequest request) {
        Pageable pageable = PageRequest.of(
                request.getPage() - 1,
                request.getPageSize(),
                Sort.by(Sort.Direction.DESC, "id")
        );

        if (request.getUsername() != null && !request.getUsername().isEmpty()) {
            List<Certificate> certificates = certificateRepository.findByUsername(request.getUsername());
            return new org.springframework.data.domain.PageImpl<>(certificates, pageable, certificates.size());
        }

        return certificateRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public List<Certificate> getUserCertificates(String username) {
        return certificateRepository.findByUsername(username);
    }

    @Transactional(readOnly = true)
    public StatisticsResponse getStatistics() {
        LocalDateTime todayStart = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);

        long totalUsers = userRepository.count();
        long totalCertificates = certificateRepository.count();
        long activeCertificates = certificateRepository.findAll().stream()
                .filter(c -> "使用中".equals(c.getState())).count();
        long revokedCertificates = crlRepository.count();

        long todayNewUsers = userRepository.findAll().stream()
                .filter(u -> u.getCreatedAt() != null && u.getCreatedAt().isAfter(todayStart))
                .count();

        long todayNewCertificates = certificateRepository.findAll().stream()
                .filter(c -> c.getCreatedAt() != null && c.getCreatedAt().isAfter(todayStart))
                .count();

        List<Map<String, Object>> certificatesByState = certificateRepository.findAll().stream()
                .collect(Collectors.groupingBy(Certificate::getState, Collectors.counting()))
                .entrySet().stream()
                .map(e -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("state", e.getKey());
                    map.put("count", e.getValue());
                    return map;
                })
                .collect(Collectors.toList());

        return StatisticsResponse.builder()
                .totalUsers(totalUsers)
                .totalCertificates(totalCertificates)
                .activeCertificates(activeCertificates)
                .revokedCertificates(revokedCertificates)
                .todayNewUsers(todayNewUsers)
                .todayNewCertificates(todayNewCertificates)
                .certificatesByState(certificatesByState)
                .build();
    }

    @Transactional(readOnly = true)
    public Page<OperationLog> getOperationLogs(Integer operatorId, String operationType,
                                                LocalDateTime startTime, LocalDateTime endTime,
                                                Integer page, Integer pageSize) {
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
        return operationLogRepository.searchLogs(operatorId, operationType, startTime, endTime, pageable);
    }

    @Transactional
    public void saveOperationLog(Integer operatorId, String operatorUsername, String operationType,
                                  String operationAction, String operationDesc,
                                  Integer targetId, String targetType) {
        OperationLog log = new OperationLog();
        log.setOperatorId(operatorId);
        log.setOperatorUsername(operatorUsername);
        log.setOperationType(operationType);
        log.setOperationAction(operationAction);
        log.setOperationDesc(operationDesc);
        log.setTargetId(targetId);
        log.setTargetType(targetType);
        log.setStatus("SUCCESS");
        operationLogRepository.save(log);
    }

    @Transactional
    public void saveOperationLog(Integer operatorId, String operatorUsername, String operationType,
                                  String operationAction, String operationDesc,
                                  Integer targetId, String targetType, String errorMsg) {
        OperationLog log = new OperationLog();
        log.setOperatorId(operatorId);
        log.setOperatorUsername(operatorUsername);
        log.setOperationType(operationType);
        log.setOperationAction(operationAction);
        log.setOperationDesc(operationDesc);
        log.setTargetId(targetId);
        log.setTargetType(targetType);
        log.setStatus("FAILED");
        log.setErrorMsg(errorMsg);
        operationLogRepository.save(log);
    }
}

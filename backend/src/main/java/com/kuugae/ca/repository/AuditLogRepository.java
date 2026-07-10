package com.kuugae.ca.repository;

import com.kuugae.ca.entity.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    Page<AuditLog> findByOperator(String operator, Pageable pageable);

    Page<AuditLog> findByOperationType(String operationType, Pageable pageable);

    Page<AuditLog> findByTimestampBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);

    @Query("SELECT a FROM AuditLog a WHERE a.targetType = ?1 AND a.targetId = ?2 ORDER BY a.timestamp DESC")
    Page<AuditLog> findByTarget(String targetType, String targetId, Pageable pageable);

    List<AuditLog> findTop10ByOrderByTimestampDesc();
}

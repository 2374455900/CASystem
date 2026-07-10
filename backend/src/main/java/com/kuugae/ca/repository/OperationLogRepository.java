package com.kuugae.ca.repository;

import com.kuugae.ca.entity.OperationLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OperationLogRepository extends JpaRepository<OperationLog, Integer> {

    Page<OperationLog> findAllByOrderByCreatedAtDesc(Pageable pageable);

    @Query("SELECT ol FROM OperationLog ol WHERE " +
           "(:operatorId IS NULL OR ol.operatorId = :operatorId) AND " +
           "(:operationType IS NULL OR ol.operationType = :operationType) AND " +
           "(:startTime IS NULL OR ol.createdAt >= :startTime) AND " +
           "(:endTime IS NULL OR ol.createdAt <= :endTime)")
    Page<OperationLog> searchLogs(@Param("operatorId") Integer operatorId,
                                  @Param("operationType") String operationType,
                                  @Param("startTime") LocalDateTime startTime,
                                  @Param("endTime") LocalDateTime endTime,
                                  Pageable pageable);

    List<OperationLog> findByOperatorIdOrderByCreatedAtDesc(Integer operatorId);

    @Query("SELECT ol.operationType, COUNT(ol) FROM OperationLog ol GROUP BY ol.operationType")
    List<Object[]> countByOperationType();

    @Query("SELECT DATE(ol.createdAt), COUNT(ol) FROM OperationLog ol WHERE ol.createdAt >= :startDate GROUP BY DATE(ol.createdAt)")
    List<Object[]> countByDate(@Param("startDate") LocalDateTime startDate);
}

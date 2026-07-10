package com.kuugae.ca.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "operation_logs")
public class OperationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "operator_id", nullable = false)
    private Integer operatorId;

    @Column(name = "operator_username", nullable = false, length = 16)
    private String operatorUsername;

    @Column(name = "operation_type", nullable = false, length = 50)
    private String operationType;

    @Column(name = "operation_action", nullable = false, length = 100)
    private String operationAction;

    @Column(name = "operation_desc", columnDefinition = "TEXT")
    private String operationDesc;

    @Column(name = "target_id")
    private Integer targetId;

    @Column(name = "target_type", length = 50)
    private String targetType;

    @Column(name = "ip_address", length = 50)
    private String ipAddress;

    @Column(name = "user_agent", length = 500)
    private String userAgent;

    @Column(name = "status", length = 20)
    private String status = "SUCCESS";

    @Column(name = "error_msg", columnDefinition = "TEXT")
    private String errorMsg;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public OperationLog() {
    }

    public OperationLog(Integer operatorId, String operatorUsername, String operationType,
                        String operationAction, String operationDesc) {
        this.operatorId = operatorId;
        this.operatorUsername = operatorUsername;
        this.operationType = operationType;
        this.operationAction = operationAction;
        this.operationDesc = operationDesc;
    }
}

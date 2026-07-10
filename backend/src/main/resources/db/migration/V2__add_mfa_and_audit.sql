-- CA 系统功能增强 - 数据库迁移脚本
-- 执行日期：2026-04-13
-- 说明：添加 MFA、审计日志等功能支持

-- 1. 更新 users 表添加 MFA 相关字段
ALTER TABLE users 
ADD COLUMN mfa_enabled TINYINT(1) DEFAULT 0 COMMENT '是否启用 MFA',
ADD COLUMN mfa_secret VARCHAR(512) DEFAULT NULL COMMENT 'MFA 密钥',
ADD COLUMN mfa_verified TINYINT(1) DEFAULT 0 COMMENT 'MFA 是否已验证';

-- 2. 创建审计日志表
CREATE TABLE IF NOT EXISTS audit_log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键 ID',
    operator VARCHAR(100) COMMENT '操作人',
    operation_type VARCHAR(50) COMMENT '操作类型',
    target_type VARCHAR(50) COMMENT '目标类型',
    target_id VARCHAR(100) COMMENT '目标 ID',
    ip_address VARCHAR(50) COMMENT 'IP 地址',
    user_agent VARCHAR(500) COMMENT '用户代理',
    result VARCHAR(20) COMMENT '操作结果',
    details TEXT COMMENT '详细信息',
    timestamp DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
    INDEX idx_operator (operator),
    INDEX idx_operation_type (operation_type),
    INDEX idx_timestamp (timestamp),
    INDEX idx_target (target_type, target_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='审计日志表';

-- 3. 创建证书提醒配置表（可选）
CREATE TABLE IF NOT EXISTS certificate_reminder_config (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '主键 ID',
    user_email VARCHAR(100) NOT NULL COMMENT '用户邮箱',
    reminder_days VARCHAR(50) DEFAULT '30,15,7' COMMENT '提醒天数配置',
    email_enabled TINYINT(1) DEFAULT 1 COMMENT '是否启用邮件提醒',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_user_email (user_email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='证书提醒配置表';

-- 4. 插入默认提醒配置（为现有用户）
INSERT INTO certificate_reminder_config (user_email, reminder_days, email_enabled)
SELECT email, '30,15,7', 1
FROM users
ON DUPLICATE KEY UPDATE updated_at = CURRENT_TIMESTAMP;

-- 迁移完成提示
SELECT '数据库迁移完成！' AS migration_status;

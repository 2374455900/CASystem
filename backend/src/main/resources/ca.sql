-- CA证书管理系统数据库脚本
-- 适用于 Spring Boot + JPA 重构版本

-- 删除旧表（如果存在）
DROP TABLE IF EXISTS `crl`;
DROP TABLE IF EXISTS `certificate`;
DROP TABLE IF EXISTS `users`;

-- 用户表
CREATE TABLE `users` (
    `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
    `username` VARCHAR(16) NOT NULL COMMENT '用户名',
    `password` VARCHAR(255) NOT NULL COMMENT '密码（BCrypt加密）',
    `email` VARCHAR(255) DEFAULT NULL COMMENT '邮箱',
    `authority` VARCHAR(10) DEFAULT '0' COMMENT '权限，0表示普通用户，1表示系统管理员',
    `created_at` DATETIME DEFAULT NULL COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT NULL COMMENT '更新时间',
    `deleted_at` DATETIME DEFAULT NULL COMMENT '删除时间（软删除）',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    UNIQUE KEY `uk_email` (`email`),
    KEY `idx_deleted_at` (`deleted_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 证书表
CREATE TABLE `certificate` (
    `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
    `serial_number` VARCHAR(255) NOT NULL COMMENT '证书序列号',
    `organization` VARCHAR(255) NOT NULL COMMENT '组织机构名称',
    `registration_number` VARCHAR(255) DEFAULT NULL COMMENT '工商注册号',
    `file_path` VARCHAR(255) DEFAULT NULL COMMENT '证书文件存储路径',
    `start_time` VARCHAR(50) DEFAULT NULL COMMENT '有效期开始时间',
    `end_time` VARCHAR(50) DEFAULT NULL COMMENT '有效期结束时间',
    `juridical_person` VARCHAR(255) DEFAULT NULL COMMENT '法人姓名',
    `charge_person` VARCHAR(255) DEFAULT NULL COMMENT '经办人姓名',
    `charge_phone` VARCHAR(20) DEFAULT NULL COMMENT '经办人电话',
    `username` VARCHAR(16) DEFAULT NULL COMMENT '所属用户名',
    `state` VARCHAR(20) DEFAULT '使用中' COMMENT '状态：使用中、已注销',
    `created_at` DATETIME DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_serial_number` (`serial_number`),
    KEY `idx_username` (`username`),
    KEY `idx_state` (`state`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='证书表';

-- 证书吊销列表表
CREATE TABLE `crl` (
    `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
    `serial_number` VARCHAR(255) NOT NULL COMMENT '被吊销的证书序列号',
    `organization` VARCHAR(255) DEFAULT NULL COMMENT '组织机构名称',
    `registration_number` VARCHAR(255) DEFAULT NULL COMMENT '工商注册号',
    `start_time` VARCHAR(50) DEFAULT NULL COMMENT '原证书有效期开始',
    `end_time` VARCHAR(50) DEFAULT NULL COMMENT '原证书有效期结束',
    `revoked_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '吊销时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_serial_number` (`serial_number`),
    KEY `idx_organization` (`organization`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='证书吊销列表';

-- 插入测试数据（可选）
-- INSERT INTO `users` (`username`, `password`, `email`, `authority`, `created_at`) 
-- VALUES ('admin', '$2a$10$...', 'admin@example.com', '1', NOW());

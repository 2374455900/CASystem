CREATE TABLE `ca_requests`
(
    `id`                     int unsigned NOT NULL AUTO_INCREMENT,
    `created_at`             datetime                                                DEFAULT NULL COMMENT '字段创建时间',
    `updated_at`             datetime                                                DEFAULT NULL COMMENT '更新时间',
    `deleted_at`             datetime                                                DEFAULT NULL COMMENT '删除时间',
    `user_id`                int                                                     NOT NULL COMMENT '申请证书的用户ID',
    `state`                  int unsigned NOT NULL COMMENT '证书状态（1：待审核， 2： 审核通过， 3：审核未通过）',
    `public_key`             text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '公钥',
    `country`                varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci  DEFAULT NULL COMMENT '国家',
    `province`               varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '州市',
    `locality`               varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '地区',
    `organization`           varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '组织',
    `organization_unit_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '部门',
    `common_name`            varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '姓名',
    `email_address`          varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '邮箱',
    PRIMARY KEY (`id`),
    KEY                      `idx_ca_requests_deleted_at` (`deleted_at`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 46
  DEFAULT CHARSET = utf8;


CREATE TABLE `certificates`
(
    `id`          int unsigned NOT NULL AUTO_INCREMENT,
    `created_at`  datetime DEFAULT NULL,
    `updated_at`  datetime DEFAULT NULL,
    `deleted_at`  datetime DEFAULT NULL,
    `user_id`     int    NOT NULL COMMENT '证书拥有者ID',
    `state`       int unsigned NOT NULL COMMENT '状态（1 代表在使用中，2代表已撤销或过期）',
    `request_id`  int    NOT NULL COMMENT '证书请求ID',
    `expire_time` bigint NOT NULL COMMENT '过期时间戳',
    PRIMARY KEY (`id`),
    KEY           `idx_certificates_deleted_at` (`deleted_at`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 39
  DEFAULT CHARSET = utf8;

CREATE TABLE `crls`
(
    `id`             int unsigned NOT NULL AUTO_INCREMENT,
    `created_at`     datetime DEFAULT NULL,
    `updated_at`     datetime DEFAULT NULL,
    `deleted_at`     datetime DEFAULT NULL,
    `certificate_id` int    NOT NULL COMMENT '证书ID',
    `input_time`     bigint NOT NULL COMMENT '加入时间戳',
    PRIMARY KEY (`id`),
    KEY              `idx_crls_deleted_at` (`deleted_at`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 17
  DEFAULT CHARSET = utf8 COMMENT ='证书吊销列表';

CREATE TABLE `user_tokens`
(
    `id`          int unsigned NOT NULL AUTO_INCREMENT,
    `created_at`  datetime DEFAULT NULL,
    `updated_at`  datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `deleted_at`  datetime DEFAULT NULL,
    `user_id`     int         NOT NULL,
    `token`       varchar(64) NOT NULL,
    `expire_time` bigint      NOT NULL,
    PRIMARY KEY (`id`),
    KEY           `idx_user_tokens_deleted_at` (`deleted_at`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 81
  DEFAULT CHARSET = utf8;

CREATE TABLE `users`
(
    `id`         int unsigned NOT NULL AUTO_INCREMENT,
    `created_at` datetime     DEFAULT NULL,
    `updated_at` datetime     DEFAULT NULL,
    `deleted_at` datetime     DEFAULT NULL,
    `username`   varchar(16)  NOT NULL,
    `password`   varchar(255) NOT NULL,
    `email`      varchar(255) DEFAULT NULL,
    `authority`  int          DEFAULT NULL COMMENT '权限，1表示系统管理员',
    PRIMARY KEY (`id`),
    UNIQUE KEY `username` (`username`),
    KEY          `idx_users_deleted_at` (`deleted_at`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 62
  DEFAULT CHARSET = utf8;


create table certificate
(
    Id                  int auto_increment
        primary key,
    serial_number       varchar(255) null,
    organization        varchar(255) null,
    registration_number varchar(255) null,
    file_path           varchar(255) null,
    start_time          varchar(255) null,
    end_time            varchar(255) null,
    juridical_person    varchar(255) null,
    charge_person       varchar(255) null,
    charge_phone        varchar(255) null,
    username            varchar(255) null
)
    charset = utf8;


create table crl
(
    Id                  int auto_increment
        primary key,
    serial_number       varchar(255) null,
    organization        varchar(255) null,
    start_time          varchar(255) null,
    end_time            varchar(255) null,
    registration_number varchar(255) null
)
    charset = utf8;

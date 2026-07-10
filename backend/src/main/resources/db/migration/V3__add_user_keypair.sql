-- CA 系统功能增强 - 用户密钥对支持
-- 执行日期：2026-04-13
-- 说明：添加用户密钥对字段支持

-- 1. 更新 certificate 表添加用户密钥对字段
ALTER TABLE certificate 
ADD COLUMN user_public_key TEXT COMMENT '用户公钥（PEM 格式）',
ADD COLUMN user_private_key_encrypted TEXT COMMENT '用户私钥（PEM 格式，加密存储）',
ADD COLUMN key_algorithm VARCHAR(20) DEFAULT 'RSA' COMMENT '密钥算法',
ADD COLUMN key_size INT DEFAULT 2048 COMMENT '密钥长度';

-- 迁移完成提示
SELECT '数据库迁移完成！用户密钥对字段已添加。' AS migration_status;

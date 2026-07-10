-- 修复 certificate 表 username 字段长度问题
-- 执行日期：2026-04-14
-- 说明：解决用户名（邮箱）过长导致的数据截断问题

-- MySQL 语法：将 username 字段长度改为 VARCHAR(255)
ALTER TABLE certificate MODIFY COLUMN username VARCHAR(255);

-- 同时修复 operation_logs 表的 operator_username 字段
ALTER TABLE operation_logs MODIFY COLUMN operator_username VARCHAR(255);

SELECT 'certificate 表和 operation_logs 表 username 字段长度修复完成！' AS migration_status;

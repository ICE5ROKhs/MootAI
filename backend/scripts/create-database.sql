-- PostgreSQL 数据库创建脚本
-- 使用方法: psql -U postgres -h 127.0.0.1 -p 5432 -f create-database.sql

-- 创建数据库（如果不存在）
SELECT 'CREATE DATABASE "MootAI"'
WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'MootAI')\gexec

-- 连接到新创建的数据库
\c "MootAI"

-- 显示数据库信息
SELECT 'Database MootAI created successfully!' AS message;


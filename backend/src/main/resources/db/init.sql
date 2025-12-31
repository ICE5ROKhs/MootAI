-- PostgreSQL 数据库初始化脚本
-- 数据库名称: MootAI
-- 主机: 127.0.0.1
-- 端口: 5432

-- 创建数据库（如果不存在）
-- 注意：需要在PostgreSQL中手动执行，或者使用psql命令
-- CREATE DATABASE "MootAI" WITH ENCODING 'UTF8';

-- 连接到MootAI数据库后执行以下语句

-- 
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- 用户表（users表将由JPA自动创建，这里提供参考结构）
-- 如果使用JPA的ddl-auto: update，表会自动创建
-- 以下是表结构的参考：

/*
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

-- 创建索引
CREATE INDEX IF NOT EXISTS idx_users_username ON users(username);
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
*/

-- 注意：由于使用了Spring Data JPA的ddl-auto: update
-- 表结构会根据Entity自动创建和更新
-- 此脚本主要用于参考和手动初始化场景


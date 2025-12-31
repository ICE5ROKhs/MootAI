# 数据库初始化说明

## PostgreSQL 数据库配置

### 数据库信息
- **数据库名称**: MootAI
- **主机地址**: 127.0.0.1
- **端口**: 5432
- **默认用户名**: postgres
- **默认密码**: 123456（请根据实际情况修改）

## 数据库创建步骤

### 方法一：使用 psql 命令行工具

1. 打开命令行/终端

2. 连接到 PostgreSQL（使用默认的 postgres 数据库）：
   ```bash
   psql -U postgres -h 127.0.0.1 -p 5432
   ```

3. 创建数据库：
   ```sql
   CREATE DATABASE "MootAI" WITH ENCODING 'UTF8';
   ```

4. 退出 psql：
   ```sql
   \q
   ```

### 方法二：使用 pgAdmin

1. 打开 pgAdmin
2. 连接到 PostgreSQL 服务器（127.0.0.1:5432）
3. 右键点击 "Databases" -> "Create" -> "Database"
4. 输入数据库名称：`MootAI`
5. 点击 "Save"

### 方法三：使用 SQL 脚本

```bash
# 使用 psql 执行 SQL 脚本
psql -U postgres -h 127.0.0.1 -p 5432 -f init.sql
```

## 数据库连接配置

数据库连接配置在 `application.yml` 文件中：

```yaml
spring:
  datasource:
    url: jdbc:postgresql://127.0.0.1:5432/MootAI
    username: postgres
    password: 123456
    driver-class-name: org.postgresql.Driver
```

## 表结构自动创建

项目使用 Spring Data JPA，配置了 `ddl-auto: update`，这意味着：

- 应用启动时会自动创建表结构
- 如果表已存在，会根据 Entity 定义自动更新表结构
- 不会删除已存在的数据

### 当前实体表

- **users**: 用户表
  - id: 主键（自增）
  - username: 用户名（唯一）
  - email: 邮箱（唯一）
  - password: 密码（加密存储）
  - created_at: 创建时间
  - updated_at: 更新时间

## 验证数据库连接

启动应用后，检查日志中是否有以下信息：
- 数据库连接成功
- 表创建/更新成功
- 没有数据库连接错误

## 常见问题

### 1. 连接被拒绝
- 检查 PostgreSQL 服务是否启动
- 检查主机地址和端口是否正确
- 检查防火墙设置

### 2. 认证失败
- 检查用户名和密码是否正确
- 检查 PostgreSQL 的 pg_hba.conf 配置

### 3. 数据库不存在
- 确保已按照上述步骤创建数据库
- 检查数据库名称是否正确（注意大小写）

### 4. 表未自动创建
- 检查 JPA 配置是否正确
- 检查 Entity 类是否正确标注
- 查看应用启动日志中的错误信息


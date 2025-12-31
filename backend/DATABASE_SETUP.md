# 数据库设置指南

## PostgreSQL 数据库配置

### 数据库信息
- **数据库名称**: MootAI
- **主机地址**: 127.0.0.1
- **端口**: 5432
- **默认用户名**: postgres
- **默认密码**: 123456（请根据实际情况修改）

## 快速设置步骤

### 1. 确保 PostgreSQL 已安装并运行

检查 PostgreSQL 服务是否运行：
- **Windows**: 打开服务管理器，查找 PostgreSQL 服务
- **Linux/Mac**: `sudo systemctl status postgresql` 或 `brew services list`

### 2. 创建数据库

#### 方法一：使用提供的脚本（推荐）

**Windows (PowerShell)**:
```powershell
cd backend/scripts
.\create-database.ps1
```

**Linux/Mac**:
```bash
cd backend/scripts
chmod +x create-database.sh
./create-database.sh
```

**使用 SQL 脚本**:
```bash
psql -U postgres -h 127.0.0.1 -p 5432 -f backend/scripts/create-database.sql
```

#### 方法二：手动创建

1. 连接到 PostgreSQL：
   ```bash
   psql -U postgres -h 127.0.0.1 -p 5432
   ```

2. 创建数据库：
   ```sql
   CREATE DATABASE "MootAI" WITH ENCODING 'UTF8';
   ```

3. 验证数据库创建：
   ```sql
   \l
   ```
   应该能看到 `MootAI` 数据库

4. 退出：
   ```sql
   \q
   ```

### 3. 配置数据库连接

#### 方式一：修改 application.yml（开发环境）

编辑 `backend/src/main/resources/application.yml`：

```yaml
spring:
  datasource:
    url: jdbc:postgresql://127.0.0.1:5432/MootAI
    username: postgres
    password: 123456  # 修改为你的实际密码
```

#### 方式二：使用本地配置文件（推荐）

1. 复制示例文件：
   ```bash
   cp backend/src/main/resources/application-local.yml.example \
      backend/src/main/resources/application-local.yml
   ```

2. 编辑 `application-local.yml`，修改密码：
   ```yaml
   spring:
     datasource:
       password: your-actual-password
   ```

#### 方式三：使用环境变量（推荐生产环境）

**Windows (PowerShell)**:
```powershell
$env:DB_PASSWORD="your-password"
$env:DB_USERNAME="postgres"
$env:DB_URL="jdbc:postgresql://127.0.0.1:5432/MootAI"
```

**Linux/Mac**:
```bash
export DB_PASSWORD=your-password
export DB_USERNAME=postgres
export DB_URL=jdbc:postgresql://127.0.0.1:5432/MootAI
```

### 4. 验证数据库连接

启动后端应用：
```bash
cd backend
mvn spring-boot:run
```

查看启动日志，应该能看到：
```
=========================================
数据库连接成功！
数据库产品: PostgreSQL
数据库版本: x.x.x
驱动名称: PostgreSQL JDBC Driver
驱动版本: x.x.x
数据库URL: jdbc:postgresql://127.0.0.1:5432/MootAI
用户名: postgres
=========================================
```

如果看到错误信息，请检查：
1. PostgreSQL 服务是否运行
2. 数据库是否已创建
3. 用户名和密码是否正确
4. 主机地址和端口是否正确

## 表结构自动创建

项目使用 Spring Data JPA，配置了 `ddl-auto: update`：

- ✅ 应用启动时会自动创建表结构
- ✅ 如果表已存在，会根据 Entity 定义自动更新表结构
- ✅ **不会删除已存在的数据**

### 当前实体表

- **users**: 用户表
  - `id`: 主键（自增）
  - `username`: 用户名（唯一，最大50字符）
  - `email`: 邮箱（唯一，最大100字符）
  - `password`: 密码（加密存储）
  - `created_at`: 创建时间
  - `updated_at`: 更新时间

## 数据库操作

### 查看表结构

连接到数据库：
```bash
psql -U postgres -h 127.0.0.1 -p 5432 -d MootAI
```

查看所有表：
```sql
\dt
```

查看表结构：
```sql
\d users
```

### 查看数据

```sql
SELECT * FROM users;
```

### 清空表数据（谨慎操作）

```sql
TRUNCATE TABLE users;
```

## 常见问题

### 1. 连接被拒绝 (Connection refused)

**原因**: PostgreSQL 服务未启动或端口不正确

**解决方法**:
- 检查 PostgreSQL 服务是否运行
- 确认端口是否为 5432
- 检查防火墙设置

### 2. 认证失败 (Authentication failed)

**原因**: 用户名或密码错误

**解决方法**:
- 检查 `application.yml` 中的用户名和密码
- 确认 PostgreSQL 的用户权限设置
- 检查 `pg_hba.conf` 配置文件

### 3. 数据库不存在 (Database does not exist)

**原因**: 数据库未创建

**解决方法**:
- 按照上述步骤创建数据库
- 检查数据库名称是否正确（注意大小写：MootAI）

### 4. 表未自动创建

**原因**: JPA 配置问题或 Entity 类问题

**解决方法**:
- 检查 `application.yml` 中的 JPA 配置
- 确认 Entity 类有 `@Entity` 注解
- 查看应用启动日志中的错误信息
- 确认 `ddl-auto` 设置为 `update`

### 5. 权限不足

**原因**: 数据库用户没有创建表的权限

**解决方法**:
```sql
-- 以超级用户身份连接
psql -U postgres -h 127.0.0.1 -p 5432

-- 授予权限
GRANT ALL PRIVILEGES ON DATABASE "MootAI" TO postgres;
```

## 生产环境建议

1. **使用环境变量**存储敏感信息（密码等）
2. **修改默认密码**，使用强密码
3. **配置连接池**参数（已在 Spring Boot 中自动配置）
4. **定期备份**数据库
5. **监控**数据库性能
6. **使用 SSL 连接**（如果可能）

## 相关文件

- 数据库初始化脚本: `backend/src/main/resources/db/init.sql`
- 数据库创建脚本: `backend/scripts/create-database.sql`
- 数据库配置说明: `backend/src/main/resources/db/README.md`
- 应用配置文件: `backend/src/main/resources/application.yml`


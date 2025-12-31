#!/bin/bash
# Shell 脚本：创建 PostgreSQL 数据库
# 使用方法: chmod +x create-database.sh && ./create-database.sh

HOST="127.0.0.1"
PORT="5432"
DATABASE="MootAI"
USERNAME="postgres"

echo "正在创建 PostgreSQL 数据库..."
echo "主机: $HOST"
echo "端口: $PORT"
echo "数据库名: $DATABASE"
echo ""

# 提示输入密码
read -sp "请输入 PostgreSQL 密码: " PASSWORD
echo ""

# 创建数据库
PGPASSWORD=$PASSWORD psql -U $USERNAME -h $HOST -p $PORT -d postgres <<EOF
SELECT 'CREATE DATABASE "$DATABASE"'
WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = '$DATABASE')\gexec
EOF

if [ $? -eq 0 ]; then
    echo "数据库 '$DATABASE' 创建成功！"
else
    echo "创建数据库时出错"
    echo "请确保 PostgreSQL 已安装并正在运行"
    exit 1
fi


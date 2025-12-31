# PowerShell 脚本：创建 PostgreSQL 数据库
# 使用方法: .\create-database.ps1

$dbHost = "127.0.0.1"
$port = "5432"
$database = "MootAI"
$username = "postgres"

Write-Host "正在创建 PostgreSQL 数据库..." -ForegroundColor Green
Write-Host "主机: $dbHost" -ForegroundColor Yellow
Write-Host "端口: $port" -ForegroundColor Yellow
Write-Host "数据库名: $database" -ForegroundColor Yellow
Write-Host ""

# 提示输入密码
$password = Read-Host "请输入 PostgreSQL 密码" -AsSecureString
$BSTR = [System.Runtime.InteropServices.Marshal]::SecureStringToBSTR($password)
$plainPassword = [System.Runtime.InteropServices.Marshal]::PtrToStringAuto($BSTR)

# 设置环境变量
$env:PGPASSWORD = $plainPassword

# 创建数据库
$sqlCommand = "SELECT 'CREATE DATABASE `"$database`"' WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = '$database')\gexec"

try {
    $result = & psql -U $username -h $dbHost -p $port -d postgres -c $sqlCommand 2>&1
    
    if ($LASTEXITCODE -eq 0) {
        Write-Host "数据库 '$database' 创建成功！" -ForegroundColor Green
    } else {
        Write-Host "创建数据库时出错: $result" -ForegroundColor Red
    }
} catch {
    Write-Host "错误: $_" -ForegroundColor Red
    Write-Host "请确保 PostgreSQL 已安装并正在运行，且 psql 命令可用" -ForegroundColor Yellow
} finally {
    # 清除密码
    Remove-Item Env:\PGPASSWORD
}


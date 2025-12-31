# 修复 PowerShell 编码问题
# 使用方法: 在启动后端之前运行此脚本，或在 PowerShell 中执行以下命令

# 设置控制台编码为 UTF-8
[Console]::OutputEncoding = [System.Text.Encoding]::UTF8
$OutputEncoding = [System.Text.Encoding]::UTF8
chcp 65001 | Out-Null

Write-Host "编码已设置为 UTF-8" -ForegroundColor Green
Write-Host "现在可以正常显示中文了" -ForegroundColor Green


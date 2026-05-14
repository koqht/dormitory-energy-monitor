@echo off
chcp 65001 >nul
title 启动后端 - 高校智能电表系统

echo ========================================
echo   高校学生公寓智能电表数据可视化系统
echo   正在启动后端服务...
echo ========================================
echo.

cd /d "%~dp0backend"

echo [1/2] 编译项目...
call mvn compile -q
if %errorlevel% neq 0 (
    echo 编译失败！请检查 JDK 和 Maven 是否安装正确。
    pause
    exit /b 1
)

echo [2/2] 启动服务 (端口 8080)...
echo.
echo 后端启动后请勿关闭此窗口。
echo 看到 "Started Application" 即启动成功。
echo 然后双击 "启动前端.bat" 启动前端。
echo ========================================
echo.

call mvn spring-boot:run

pause

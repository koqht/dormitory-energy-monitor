@echo off
chcp 65001 >nul
title 启动前端 - 高校智能电表系统

echo ========================================
echo   高校学生公寓智能电表数据可视化系统
echo   正在启动前端页面...
echo ========================================
echo.

cd /d "%~dp0frontend"

if not exist "node_modules\" (
    echo [1/2] 首次运行，正在安装前端依赖（约 1-2 分钟）...
    call npm install
    echo.
)

echo [2/2] 启动前端开发服务器 (端口 3000)...
echo.
echo 启动后请勿关闭此窗口。
echo 浏览器访问 http://localhost:3000 即可使用系统。
echo ========================================
echo.

call npm run dev

pause

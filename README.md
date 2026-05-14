# 高校学生公寓智能电表数据可视化系统



---

## 一、安装 4 个必备软件

下面 4 个软件都需要装上。

### 1. Java（JDK）

去 Oracle 官网下载 JDK 8 安装包：

https://www.oracle.com/java/technologies/downloads/#java8-windows

- 选 **Windows x64 Installer**（一般是 `jdk-8uXXX-windows-x64.exe`）
- 下载后双击 → 一路点 **下一步** 到完成
- 验证：打开 Windows 搜索框 → 输入 `cmd` → 回车 → 输入 `java -version` → 看到版本号就 OK

### 2. Node.js

去 Node.js 官网下载 LTS 版本：

https://nodejs.org

- 左边绿色按钮 "LTS" → 下载 → 双击安装
- 一路点 **Next** 到完成，所有选项默认即可
- 验证：打开 cmd → 输入 `node -v` → 看到版本号就 OK

### 3. MySQL 数据库

去 MySQL 官网下载社区版：

https://dev.mysql.com/downloads/installer/

- 下载 `mysql-installer-community-5.7.xx.msi`（或 8.0 版本）
- 双击安装 → 选 **Developer Default** → 一路 Next
- **关键一步**：安装过程中会让你设置 root 密码，记下来（比如设为 `123456`）
- 验证：打开 cmd → 输入 `mysql -u root -p` → 输入密码 → 看到 `mysql>` 就 OK


### 4. Maven

去 Maven 官网下载：

https://maven.apache.org/download.cgi

- 下载 **Binary zip archive**（`apache-maven-3.8.8-bin.zip`）
- 解压到 `C:\Program Files\` 或你喜欢的任何位置
- 配置环境变量（见下方）

**配置 Maven 环境变量（这一步稍微复杂，照着做就行）：**

```
1. 右键"此电脑" → 属性 → 高级系统设置 → 环境变量
2. 在"系统变量"里点"新建"：
   变量名：MAVEN_HOME
   变量值：C:\Program Files\apache-maven-3.8.8  （改成你实际解压的路径）
3. 找到"系统变量"里的 Path → 编辑 → 新建 → 输入：
   %MAVEN_HOME%\bin
4. 全部点"确定"关闭
```

验证：打开 cmd → 输入 `mvn -version` → 看到版本号就 OK。

---

## 二、下载项目并导入数据库

### 1. 下载代码

在 GitHub 项目页面点击绿色 **Code** 按钮 → **Download ZIP**：

https://github.com/koqht/dormitory-energy-monitor

下载后解压到桌面（或你喜欢的任何位置）。

### 2. 导入数据库

打开 cmd（Windows 搜索框 → 输入 `cmd` → 回车），输入：

```
mysql -u root -p
```

输入你安装 MySQL 时设置的密码，进入 `mysql>` 提示符后：

```
source C:\Users\你的用户名\Desktop\dormitory-energy-monitor\backend\src\main\resources\init.sql
```

> 上面路径改成你实际解压的位置。执行完就自动创建好数据库和测试数据了。

### 3. 修改数据库密码配置

用记事本打开项目里的这个文件：

```
backend\src\main\resources\application.yml
```

找到这两行，改成你的 MySQL 用户名和密码：

```yaml
username: root      # 你的MySQL用户名（默认就是root）
password: 123456    # 改成你设置的密码
```

保存关闭。

---

## 三、启动项目

### 方式一：双击启动脚本（推荐）

Windows 用户双击项目根目录下的 **`启动后端.bat`**，等它显示 "Started Application" 后，再双击 **`启动前端.bat`**。

### 方式二：手动分别启动

**启动后端：**打开 cmd，cd 到项目里的 `backend` 目录，输入：
```
mvn spring-boot:run
```
看到 `Started Application in xxx seconds` 就表示后端跑起来了。

**启动前端：**再打开一个 cmd，cd 到项目里的 `frontend` 目录，输入：
```
npm install
npm run dev
```
看到 `Local: http://localhost:3000/` 就表示前端跑起来了。

---

## 四、打开浏览器使用

浏览器访问 **http://localhost:3000**

### 测试账号

| 角色 | 账号 | 密码 | 看到什么 |
|------|------|------|----------|
| 系统管理员 | `admin001` | `admin123` | 用户管理、宿舍管理、电表管理 |
| 宿管员 | `staff001` | `admin123` | 数据概览、用电查询、预警处理、账单 |
| 学生 | `20210001` | `123456` | 个人仪表盘、用电图表、账单、预警 |

---

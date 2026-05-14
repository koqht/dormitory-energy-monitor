# 高校学生公寓智能电表数据可视化系统

基于 Spring Boot + Vue 3 + MySQL + ECharts 的宿舍用电管理系统。

## 环境要求

| 软件 | 版本 | 说明 |
|------|------|------|
| JDK | 1.8+ | `java -version` 确认 |
| Maven | 3.0.5+ | `mvn -version` 确认 |
| MySQL | 5.7+ | `mysql --version` 确认，需创建数据库 |
| Node.js | 16+ | `node -v` 确认，用于前端 |

## 快速开始

### 1. 克隆项目

```bash
git clone https://github.com/koqht/dormitory-energy-monitor.git
cd dormitory-energy-monitor
```

### 2. 初始化数据库

启动 MySQL 后，执行项目中的初始化脚本：

```bash
mysql -u root -p < backend/src/main/resources/init.sql
```

这会自动创建 `dormitory_monitor` 数据库，并插入测试数据（15 间宿舍、450 条用电记录、预警和账单）。

> 如果已经创建过数据库，先 `DROP DATABASE dormitory_monitor;` 再导入。

### 3. 配置数据库连接

编辑 `backend/src/main/resources/application.yml`，修改数据库用户名和密码：

```yaml
spring:
  datasource:
    username: root      # 改成你的 MySQL 用户名
    password: 123456    # 改成你的 MySQL 密码
```

### 4. 启动后端

```bash
cd backend
mvn spring-boot:run
```

后端启动在 `http://localhost:8080`

### 5. 启动前端

```bash
cd frontend
npm install
npm run dev
```

前端启动在 `http://localhost:3000`

### 6. 登录系统

| 角色 | 账号 | 密码 |
|------|------|------|
| 系统管理员 | admin001 | admin123 |
| 宿管员 | staff001 | admin123 |
| 学生 | 20210001 | 123456 |

## 项目结构

```
├── backend/                  # Spring Boot 后端
│   ├── pom.xml
│   └── src/main/
│       ├── java/com/dormitory/
│       │   ├── controller/   # REST 控制器
│       │   ├── service/      # 业务逻辑
│       │   ├── mapper/       # MyBatis 数据访问
│       │   ├── entity/       # 实体类
│       │   └── config/       # 配置
│       └── resources/
│           ├── application.yml
│           └── init.sql       # 数据库初始化
├── frontend/                 # Vue 3 前端
│   └── src/
│       ├── views/            # 页面组件
│       ├── router/           # 路由
│       └── api/              # API 封装
└── PROJECT_DOCUMENTATION.md  # 详细项目文档
```

## 常见问题

**Q: Maven 编译报 "Plugin requires Maven version 3.x"？**  
A: 你用的是 Maven 3.0.5 以下的旧版本，升级到 3.6+ 即可：
1. 下载 [Maven 3.8.8](https://maven.apache.org/download.cgi)
2. 解压后配置环境变量 `MAVEN_HOME` 指向新目录

**Q: 前端启动报 "element-plus" 找不到？**  
A: 缺少依赖，运行 `cd frontend && npm install` 安装即可。

**Q: 新增用户后列表中看不到？**  
A: 已修复。默认查询所有用户，无需翻页。

# 博客系统 (Blog System)

一个基于 Spring Boot + MyBatis + Redis 的全栈博客管理系统，支持文章发布、Markdown 编辑、评论互动等核心功能。

## 📋 项目信息

- **项目名称**: Blog System
- **版本**: 0.0.1-SNAPSHOT
- **作者**: 王进超
- **开发语言**: Java 17
- **核心框架**: Spring Boot 2.7.17

## 🚀 技术栈

### 后端技术
- **核心框架**: Spring Boot 2.7.17
- **持久层框架**: MyBatis + PageHelper 分页插件
- **数据库**: MySQL 8.0.33
- **缓存**: Redis (Spring Data Redis)
- **安全框架**: Spring Security
- **API 文档**: Knife4j 3.0.3 (Swagger)
- **模板引擎**: Thymeleaf
- **邮件服务**: Spring Boot Starter Mail
- **AOP**: Spring Boot Starter AOP

### 前端技术
- **模板引擎**: Thymeleaf
- **UI 框架**: Amaze UI
- **JavaScript 库**: jQuery
- **Markdown 编辑器**: 支持 GFM 表格扩展

### 工具库
- **Lombok**: 简化 Java 代码
- **Apache Commons Lang3**: 字符串工具
- **Emoji Java**: Emoji 表情过滤
- **Commonmark**: Markdown 解析

## ✨ 核心功能

### 用户端
- 📖 文章列表展示（分页）
- 📝 文章详情查看
- 💬 评论发表与管理
- 📊 文章浏览统计
- 🏷️ 分类标签筛选

### 管理后台
- ✍️ 文章发布与编辑（Markdown）
- 🗑️ 文章删除管理
- 📁 分类管理
- 📈 数据统计展示
- 🔒 安全认证控制

## 📂 项目结构

```
blog/
├── src/main/java/com/wjc/
│   ├── BolgSystemApplication.java      # 启动类
│   ├── config/                         # 配置类
│   │   ├── RedisConfig.java           # Redis 配置
│   │   ├── SecurityConfig.java        # 安全配置
│   │   ├── SwaggerConfig.java         # API 文档配置
│   │   └── WebMvcConfig.java          # MVC 配置
│   ├── dao/                            # 数据访问层
│   │   ├── ArticleMapper.java
│   │   ├── CategoriesMapper.java
│   │   ├── CommentMapper.java
│   │   └── StatisticMapper.java
│   ├── interceptor/                    # 拦截器
│   │   └── BaseInterceptor.java
│   ├── model/                          # 数据模型
│   │   ├── domain/                    # 领域模型
│   │   └── ResponseData/              # 响应数据封装
│   ├── scheduletask/                   # 定时任务
│   │   └── ScheduleTask.java
│   ├── service/                        # 业务逻辑层
│   │   └── impl/                      # 实现类
│   ├── utils/                          # 工具类
│   │   ├── Commons.java               # 通用工具
│   │   ├── MailUtils.java             # 邮件工具
│   │   └── MyUtils.java               # 自定义工具
│   └── web/                            # 控制层
│       ├── admin/                     # 后台控制器
│       └── client/                    # 前台控制器
├── src/main/resources/
│   ├── mapper/                         # MyBatis XML 映射文件
│   ├── static/                         # 静态资源
│   ├── templates/                      # Thymeleaf 模板
│   │   ├── back/                      # 后台页面
│   │   ├── client/                    # 前台页面
│   │   └── comm/                      # 公共页面
│   ├── i18n/                          # 国际化资源
│   ├── application.yml                 # 主配置文件
│   └── application-redis.properties    # Redis 配置
├── bolg_system.sql                     # 数据库脚本
├── pom.xml                             # Maven 依赖配置
└── 项目介绍.md                         # 项目详细介绍
```

## 🔧 环境要求

- **JDK**: 17+
- **Maven**: 3.6+
- **MySQL**: 8.0+
- **Redis**: 6.0+

## 📦 快速开始

### 1. 克隆项目

```bash
git clone <your-repository-url>
cd blog
```

### 2. 配置数据库

创建数据库并导入脚本：

```bash
mysql -u root -p
CREATE DATABASE bolg_system DEFAULT CHARSET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE bolg_system;
SOURCE bolg_system.sql;
```

### 3. 修改配置

编辑 `src/main/resources/application.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/bolg_system?useSSL=false&serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf8&allowPublicKeyRetrieval=true
    username: root
    password: your_password
```

编辑 `src/main/resources/application-redis.properties`：

```properties
spring.redis.host=127.0.0.1
spring.redis.port=6379
spring.redis.password=
```

### 4. 启动 Redis

```bash
# Windows
redis-server.exe

# Linux/Mac
redis-server
```

### 5. 构建运行

```bash
# 使用 Maven 构建
mvn clean install

# 运行项目
mvn spring-boot:run
```

或者直接运行启动类 `BolgSystemApplication.java`

### 6. 访问应用

- **前台首页**: http://localhost:8080/
- **后台管理**: http://localhost:8080/admin
- **API 文档**: http://localhost:8080/doc.html

## 🔐 安全特性

- ✅ Spring Security 认证授权
- ✅ XSS 脚本过滤
- ✅ SQL 注入防护
- ✅ Emoji 表情安全处理
- ✅ Cookie 会话管理（30分钟有效期）

## 📖 API 文档

项目集成了 Knife4j，启动后访问 http://localhost:8080/doc.html 查看完整 API 文档。

### 主要接口

#### 管理端
- `GET /admin/index` - 管理后台首页
- `GET /admin/article` - 文章列表
- `POST /admin/article/publish` - 发布文章
- `POST /admin/article/modify` - 修改文章
- `POST /admin/article/delete` - 删除文章

#### 用户端
- `GET /` - 首页文章列表
- `POST /comments/publish` - 发表评论
- `POST /comments/getByid` - 获取评论列表
- `POST /comments/delete` - 删除评论

## 🛠️ 开发指南

### 构建项目

```bash
mvn clean package
```

### 运行测试

```bash
mvn test
```

### 生成 JAR 包

```bash
mvn clean package -DskipTests
java -jar target/bolg_system-0.0.1-SNAPSHOT.jar
```

## 📝 数据库配置

- **数据库名**: bolg_system
- **字符编码**: UTF-8
- **时区**: Asia/Shanghai
- **连接池**: HikariCP

## 🎯 未来计划

- [ ] 完善评论管理后台
- [ ] 文章分类/标签管理优化
- [ ] 系统设置功能
- [ ] 文章评论开关控制
- [ ] 评论回复功能
- [ ] 用户注册功能
- [ ] 文章搜索功能

## 📄 许可证

本项目遵循 MIT 许可证。

## 📧 联系方式

- **作者**: 王进超
- **邮箱**: 3606195422@qq.com

---

**最后更新**: 2026年1月

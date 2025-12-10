# JavaBlog

基于 Java 的博客系统，采用现代化的 Spring MVC + MyBatis 架构，支持文章管理、Markdown 编辑、分类标签、评论功能等完整的博客系统特性。

## 项目简介

JavaBlog 是一个功能完整的博客管理系统，适合用于学习 Java Web 开发和博客系统设计。项目采用经典的 MVC 架构模式，前后端分离的设计理念，提供了丰富的博客管理功能。

### 主要特性

- ✨ **文章管理**：支持文章的创建、编辑、删除、查询功能
- 📝 **Markdown 编辑**：集成 Markdown 编辑器，支持实时预览
- 🏷️ **分类标签**：支持文章分类和标签管理
- 💬 **评论系统**：支持文章评论功能，可控制是否允许评论
- 🔍 **搜索功能**：支持关键词搜索和分类筛选
- 🖼️ **缩略图上传**：支持文章缩略图上传和管理
- 👤 **管理员认证**：基于 Session 的用户认证和权限管理
- 🎨 **响应式界面**：采用 Bootstrap 和 Hexo Butterfly 主题样式

## 技术栈

### 后端技术

- **Spring MVC 7.0.1**：Web 框架，基于 Jakarta Servlet 5.0（支持 Tomcat 10+）
- **MyBatis 3.5.14**：持久层框架，用于数据库操作
- **MySQL**：关系型数据库（使用 mysql-connector-j 9.4.0）
- **HikariCP 5.1.0**：高性能数据库连接池
- **Lombok 1.18.38**：简化 Java 代码
- **Log4j 1.2.17**：日志框架

### 前端技术

- **JSP + JSTL**：页面模板技术
- **Bootstrap**：响应式 CSS 框架
- **EasyMDE**：Markdown 编辑器
- **Font Awesome**：图标库
- **Hexo Butterfly**：主题样式

### 其他依赖

- **Flexmark 0.64.0**：Markdown 解析库
- **Jsoup 1.16.1**：HTML 解析库
- **JUnit**：单元测试框架

## 项目结构

```
javablog/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/
│   │   │       ├── bean/          # 实体类（Article, User, Comment 等）
│   │   │       ├── controller/    # Spring MVC 控制器
│   │   │       ├── dao/           # MyBatis Mapper 接口
│   │   │       ├── servlet/       # 传统 Servlet（部分功能）
│   │   │       ├── filter/        # 过滤器（认证、数据预处理）
│   │   │       ├── util/          # 工具类（文章处理、用户管理等）
│   │   │       └── config/        # 配置类（数据源等）
│   │   ├── resources/
│   │   │   ├── mapper/            # MyBatis XML 映射文件
│   │   │   ├── mybatis-config.xml # MyBatis 配置文件
│   │   │   ├── spring-mvc.xml     # Spring MVC 配置文件
│   │   │   ├── db.properties      # 数据库配置
│   │   │   └── log4j.properties   # 日志配置
│   │   └── webapp/
│   │       ├── WEB-INF/
│   │       │   └── web.xml        # Web 应用配置
│   │       ├── css/               # 样式文件
│   │       ├── images/            # 图片资源
│   │       ├── img/               # 主题图片资源
│   │       ├── *.jsp              # JSP 页面文件
│   │       └── favicon.ico        # 网站图标
│   └── test/                      # 测试代码
├── pom.xml                        # Maven 项目配置
└── README.md                      # 项目说明文档
```

### 核心模块说明

- **bean**：实体类，包含 Article（文章）、AdminUser（管理员）、User（用户）、Comment（评论）等
- **controller**：Spring MVC 控制器，处理 HTTP 请求
  - `IndexController`：首页控制器
  - `LoginController`：登录认证控制器
  - `ArticleController`：文章管理控制器
- **dao**：MyBatis Mapper 接口，定义数据库操作
- **util**：工具类，包含文章处理、Markdown 转换、用户管理等功能
- **filter**：过滤器，处理认证和数据预处理

## 环境要求

### 必需环境

- **JDK 17** 或更高版本（项目使用 Jakarta EE 9+ API）
- **Maven 3.6+**：用于项目构建和依赖管理
- **MySQL 5.7+** 或 **MySQL 8.0+**：数据库服务器
- **Tomcat 10+** 或其他支持 Jakarta Servlet 5.0 的应用服务器

### 推荐开发工具

- IntelliJ IDEA / Eclipse / VS Code
- MySQL Workbench 或其他数据库管理工具

## 快速开始

### 1. 数据库配置

#### 创建数据库

```sql
CREATE DATABASE blog_sys CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

#### 创建表结构

```sql
USE blog_sys;

-- 管理员用户表
CREATE TABLE admin_user (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    created DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 文章表
CREATE TABLE article (
    id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    content TEXT,
    markdown TEXT,
    created DATETIME DEFAULT CURRENT_TIMESTAMP,
    modified DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    categories VARCHAR(100),
    tags VARCHAR(255),
    allow_comment BOOLEAN DEFAULT TRUE,
    thumbnail VARCHAR(255)
);

-- 评论表
CREATE TABLE comment (
    id INT PRIMARY KEY AUTO_INCREMENT,
    article_id INT NOT NULL,
    author VARCHAR(50) NOT NULL,
    email VARCHAR(100),
    content TEXT NOT NULL,
    created DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (article_id) REFERENCES article(id) ON DELETE CASCADE
);

-- 插入默认管理员账号（用户名: admin, 密码: admin）
INSERT INTO admin_user (username, password) VALUES ('admin', 'admin');
```

#### 配置数据库连接

编辑 `src/main/resources/db.properties` 文件，配置数据库连接信息：

```properties
mysql.driver=com.mysql.cj.jdbc.Driver
mysql.url=jdbc:mysql://localhost:3306/blog_sys?useSSL=false&serverTimezone=UTC
mysql.username=root
mysql.password=your_password
```

**注意**：将 `your_password` 替换为你的 MySQL 密码。

### 2. 构建项目

在项目根目录执行以下命令：

```bash
mvn clean package
```

构建成功后，会在 `target/` 目录下生成 `9.23javaee.war` 文件。

### 3. 部署运行

#### 方式一：使用 Tomcat

1. 将 `target/9.23javaee.war` 复制到 Tomcat 的 `webapps/` 目录下
2. 启动 Tomcat 服务器
3. 访问 `http://localhost:8080/9.23javaee/`

#### 方式二：使用 Maven 插件（开发环境）

可以使用 Maven Tomcat 插件直接运行：

```bash
mvn tomcat7:run
```

或者配置 IDE 的运行配置，直接在 IDE 中启动。

### 4. 访问系统

- **首页**：`http://localhost:8080/9.23javaee/index`
- **管理后台**：`http://localhost:8080/9.23javaee/login`
  - 默认用户名：`admin`
  - 默认密码：`admin`

## 功能模块

### 1. 前台展示

- **首页**：展示所有文章列表，支持分类和关键词搜索
- **文章详情**：查看文章完整内容，显示 Markdown 渲染后的 HTML
- **分类筛选**：按分类浏览文章
- **评论展示**：查看文章评论（如果允许评论）

### 2. 后台管理

- **登录认证**：管理员登录系统
- **文章管理**：
  - 创建新文章（支持 Markdown 编辑）
  - 编辑现有文章
  - 删除文章
  - 搜索文章
  - 上传文章缩略图
- **分类标签**：为文章设置分类和标签
- **评论控制**：设置文章是否允许评论

### 3. Markdown 支持

- 使用 Flexmark 库将 Markdown 转换为 HTML
- 支持标准 Markdown 语法
- 实时预览功能（前端）

## 开发指南

### 代码规范

- 遵循 Java 编码规范
- 使用 Lombok 简化实体类代码
- Controller 层处理请求，Service/Util 层处理业务逻辑
- DAO 层使用 MyBatis 进行数据库操作

### 日志配置

项目使用 Log4j 进行日志记录，配置文件位于 `src/main/resources/log4j.properties`。

### 添加新功能

1. 在 `bean` 包中定义实体类
2. 在 `dao` 包中创建 Mapper 接口
3. 在 `resources/mapper` 中编写 MyBatis XML 映射文件
4. 在 `util` 或 `service` 包中实现业务逻辑
5. 在 `controller` 包中创建控制器处理请求
6. 创建或修改 JSP 页面

### 数据库迁移

如果需要修改数据库表结构，建议：

1. 备份现有数据库
2. 编写 SQL 迁移脚本
3. 更新实体类和 Mapper 文件
4. 测试新功能

## 常见问题

### Q: 部署后无法访问页面？

A: 检查以下几点：
- Tomcat 是否正常启动
- WAR 包是否正确部署到 webapps 目录
- 访问地址是否正确（包含 context path）
- 防火墙是否开放 8080 端口

### Q: 数据库连接失败？

A: 检查：
- MySQL 服务是否启动
- `db.properties` 中的连接信息是否正确
- 数据库名称是否存在
- 用户名和密码是否正确
- MySQL 驱动版本是否兼容

### Q: 登录后无法访问管理页面？

A: 确保：
- 数据库中有管理员账号记录
- Session 配置正常
- 过滤器（AuthFilter）正确配置

### Q: Markdown 文章显示异常？

A: 检查：
- Markdown 语法是否正确
- Flexmark 依赖是否正确加载
- 前端 CSS 样式是否加载

### Q: 文件上传失败？

A: 确认：
- `web.xml` 中 multipart-config 配置正确
- 上传文件大小不超过限制（默认 20MB）
- 上传目录有写入权限

## 性能优化建议

- 使用 HikariCP 连接池提高数据库性能
- 对频繁查询的数据添加缓存
- 优化 MyBatis SQL 语句
- 使用 CDN 加载静态资源
- 启用 Gzip 压缩

## 安全建议

- **修改默认管理员密码**：首次部署后立即修改
- **密码加密**：建议使用 BCrypt 或其他加密算法存储密码
- **SQL 注入防护**：使用 MyBatis 参数化查询
- **XSS 防护**：对用户输入进行过滤和转义
- **CSRF 防护**：添加 CSRF Token 验证

## 许可证

本项目仅供学习和研究使用。

## 贡献

欢迎提交 Issue 和 Pull Request 来改进这个项目。

## 联系方式

如有问题或建议，请通过 GitHub Issues 联系。

---

**注意**：本项目主要用于教学和学习目的，生产环境使用前请进行充分的安全加固和性能测试。

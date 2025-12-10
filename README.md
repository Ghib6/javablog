# JavaBlog

<div align="center">
  <h3>基于 Java EE 的现代化博客系统</h3>
  <p>一个功能完整的博客管理系统，支持 Markdown 编辑、文章管理、分类标签、评论系统等功能</p>
</div>

---

## 📋 目录

- [项目简介](#项目简介)
- [主要特性](#主要特性)
- [技术栈](#技术栈)
- [项目结构](#项目结构)
- [环境要求](#环境要求)
- [快速开始](#快速开始)
- [数据库配置](#数据库配置)
- [构建与部署](#构建与部署)
- [功能说明](#功能说明)
- [API 接口](#api-接口)
- [开发指南](#开发指南)
- [常见问题](#常见问题)
- [贡献指南](#贡献指南)
- [许可证](#许可证)

---

## 项目简介

JavaBlog 是一个基于 Java EE 技术栈开发的现代化博客管理系统。该项目采用经典的 MVC 架构模式，结合 Spring MVC、MyBatis 等主流框架，为学习 Java Web 开发提供了一个完整的实战案例。

项目前端采用了 Hexo Butterfly 主题风格，提供了美观的用户界面和良好的用户体验。无论是作为学习项目还是实际应用，都具有很高的参考价值。

---

## 主要特性

### 核心功能

- ✅ **文章管理**
  - 创建、编辑、删除文章
  - Markdown 编辑器支持
  - 实时预览功能
  - 文章缩略图上传
  - 文章分类和标签
  
- ✅ **用户系统**
  - 管理员登录/登出
  - 访问权限控制
  - 会话管理

- ✅ **评论系统**
  - 文章评论功能
  - 评论开关控制
  - 评论列表展示

- ✅ **搜索功能**
  - 关键词搜索
  - 分类筛选
  - 标签筛选

- ✅ **文件管理**
  - 图片上传
  - 缩略图管理
  - 文件大小限制（单文件最大 20MB）

### 技术特性

- 📱 响应式设计，支持移动端访问
- 🎨 Hexo Butterfly 主题风格
- 🔒 安全的用户认证和授权
- 🚀 高性能数据库连接池（HikariCP）
- 📝 完整的 Markdown 渲染支持
- 🔍 灵活的文章搜索和分类

---

## 技术栈

### 后端技术

| 技术 | 版本 | 说明 |
|------|------|------|
| Java | 11+ | 核心开发语言 |
| Jakarta EE | 5.0 | Servlet API |
| Spring MVC | 7.0.1 | MVC 框架 |
| MyBatis | 3.5.14 | 持久层框架 |
| MySQL | 8.0+ | 关系型数据库 |
| HikariCP | 5.1.0 | 数据库连接池 |
| Lombok | 1.18.38 | 简化 Java 代码 |
| Flexmark | 0.64.0 | Markdown 解析 |
| Jsoup | 1.16.1 | HTML 解析 |
| Log4j | 1.2.17 | 日志框架 |

### 前端技术

- **JSP** - 页面模板引擎
- **JSTL** - JSP 标签库
- **JavaScript** - 前端交互
- **CSS3** - 样式设计
- **Hexo Butterfly Theme** - UI 主题

### 构建工具

- **Maven 3.x** - 项目构建和依赖管理
- **Tomcat 10+** - 应用服务器（推荐）

---

## 项目结构

```
javablog/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/example/
│       │       ├── bean/              # 实体类
│       │       │   ├── Article.java   # 文章实体
│       │       │   ├── AdminUser.java # 管理员实体
│       │       │   ├── Comment.java   # 评论实体
│       │       │   └── User.java      # 用户实体
│       │       ├── controller/        # Spring MVC 控制器
│       │       │   ├── ArticleController.java
│       │       │   ├── IndexController.java
│       │       │   └── LoginController.java
│       │       ├── dao/               # MyBatis Mapper 接口
│       │       │   ├── ArticleMapper.java
│       │       │   └── AdminUserMapper.java
│       │       ├── servlet/           # Servlet 类
│       │       │   ├── ArticleServlet.java
│       │       │   ├── IndexServlet.java
│       │       │   └── LoginServlet.java
│       │       ├── filter/            # 过滤器
│       │       │   ├── AuthFilter.java      # 认证过滤器
│       │       │   └── IndexDataFilter.java # 数据过滤器
│       │       ├── util/              # 工具类
│       │       │   ├── ArticleUtil.java
│       │       │   ├── AdminUserUtil.java
│       │       │   ├── MarkdownService.java
│       │       │   ├── MyBatisUtils.java
│       │       │   └── Util.java
│       │       └── config/            # 配置类
│       │           └── DataSourceProvider.java
│       ├── resources/
│       │   ├── mapper/                # MyBatis XML 映射文件
│       │   │   └── AdminUserMapper.xml
│       │   ├── mybatis-config.xml     # MyBatis 配置
│       │   ├── spring-mvc.xml         # Spring MVC 配置
│       │   ├── db.properties          # 数据库配置
│       │   └── log4j.properties       # 日志配置
│       └── webapp/
│           ├── WEB-INF/
│           │   └── web.xml            # Web 应用配置
│           ├── css/                   # 样式文件
│           ├── js/                    # JavaScript 文件
│           ├── images/                # 图片资源
│           ├── img/                   # 主题图片
│           ├── fonts/                 # 字体文件
│           ├── uploads/               # 用户上传文件
│           ├── index.jsp              # 首页
│           ├── login.jsp              # 登录页
│           ├── addArticle.jsp         # 添加/编辑文章页
│           ├── articleslist.jsp       # 文章列表页
│           └── viewArticle.jsp        # 文章详情页
├── pom.xml                            # Maven 配置文件
└── README.md                          # 项目说明文档
```

---

## 环境要求

### 必需环境

- **JDK**: 11 或更高版本
- **Maven**: 3.6 或更高版本
- **MySQL**: 8.0 或更高版本
- **应用服务器**: Tomcat 10+ 或其他支持 Jakarta EE 的服务器

### 开发工具（推荐）

- **IDE**: IntelliJ IDEA / Eclipse / VS Code
- **数据库管理工具**: MySQL Workbench / Navicat / DBeaver
- **API 测试工具**: Postman / Apifox

---

## 快速开始

### 1. 克隆项目

```bash
git clone https://github.com/Ghib6/javablog.git
cd javablog
```

### 2. 配置数据库

创建数据库：

```sql
CREATE DATABASE blog_sys CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE blog_sys;
```

创建表结构：

```sql
-- 管理员用户表
CREATE TABLE admin_user (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 文章表
CREATE TABLE article (
    id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(200) NOT NULL,
    content TEXT,
    markdown TEXT,
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    categories VARCHAR(100),
    tags VARCHAR(200),
    allow_comment BOOLEAN DEFAULT TRUE,
    thumbnail VARCHAR(255)
);

-- 评论表
CREATE TABLE comment (
    id INT PRIMARY KEY AUTO_INCREMENT,
    article_id INT NOT NULL,
    author VARCHAR(50),
    content TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (article_id) REFERENCES article(id) ON DELETE CASCADE
);

-- 插入默认管理员账号（密码: admin123）
INSERT INTO admin_user (username, password) VALUES ('admin', 'admin123');
```

### 3. 修改配置文件

编辑 `src/main/resources/db.properties`：

```properties
mysql.driver=com.mysql.cj.jdbc.Driver
mysql.url=jdbc:mysql://localhost:3306/blog_sys?useSSL=false&serverTimezone=UTC
mysql.username=root
mysql.password=你的数据库密码
```

### 4. 构建项目

```bash
mvn clean package
```

### 5. 部署运行

将生成的 WAR 文件（`target/9.23javaee.war`）部署到 Tomcat 服务器：

```bash
# 方法 1: 复制到 Tomcat webapps 目录
cp target/9.23javaee.war $TOMCAT_HOME/webapps/

# 方法 2: 使用 IDE 内置的服务器运行
# 在 IntelliJ IDEA 中配置 Tomcat 服务器并运行
```

### 6. 访问应用

启动 Tomcat 后，在浏览器中访问：

```
http://localhost:8080/9.23javaee/
```

默认管理员账号：
- 用户名：`admin`
- 密码：`admin123`

---

## 数据库配置

### 连接池配置

项目使用 HikariCP 作为数据库连接池，配置位于 `DataSourceProvider.java`。默认配置：

- **最大连接数**: 10
- **连接超时**: 30000ms
- **空闲超时**: 600000ms
- **最大生命周期**: 1800000ms

### MyBatis 配置

MyBatis 配置文件位于 `src/main/resources/mybatis-config.xml`，包含：

- 类型别名配置
- 插件配置
- Mapper 映射文件位置

---

## 构建与部署

### Maven 构建命令

```bash
# 清理编译
mvn clean

# 编译源码
mvn compile

# 运行测试
mvn test

# 打包项目
mvn package

# 跳过测试打包
mvn package -DskipTests

# 清理并打包
mvn clean package
```

### 部署到 Tomcat

#### 方式一：WAR 部署

1. 构建 WAR 包：`mvn clean package`
2. 将 `target/9.23javaee.war` 复制到 `$TOMCAT_HOME/webapps/`
3. 启动 Tomcat：`$TOMCAT_HOME/bin/startup.sh`
4. 访问：`http://localhost:8080/9.23javaee/`

#### 方式二：IDE 部署

在 IntelliJ IDEA 中：

1. 配置 Tomcat 服务器：Run -> Edit Configurations -> + -> Tomcat Server -> Local
2. 设置应用程序服务器路径
3. 在 Deployment 标签页添加项目 artifact
4. 点击运行按钮启动

---

## 功能说明

### 1. 用户认证

- **登录页面**: `/login.jsp`
- **认证接口**: `/login`
- **登出接口**: `/logout`
- **权限过滤**: AuthFilter 拦截未授权访问

### 2. 文章管理

#### 文章列表
- **访问路径**: `/showArticlelist`
- **功能**: 显示所有文章、分类统计、搜索功能

#### 创建文章
- **访问路径**: `/addArticle.jsp`
- **功能**: 
  - Markdown 编辑器
  - 标题、分类、标签设置
  - 缩略图上传
  - 评论开关控制

#### 编辑文章
- **访问路径**: `/article?action=edit&id={文章ID}`
- **功能**: 修改已有文章内容

#### 删除文章
- **访问路径**: `/article?action=delete&id={文章ID}`
- **功能**: 删除指定文章

#### 查看文章
- **访问路径**: `/article?action=view&id={文章ID}`
- **功能**: 
  - 文章内容展示
  - Markdown 渲染
  - 评论列表
  - 文章元信息

### 3. 搜索功能

- **关键词搜索**: 搜索文章标题和内容
- **分类筛选**: 按文章分类过滤
- **组合搜索**: 关键词 + 分类组合查询

### 4. 评论系统

- 支持文章评论
- 评论列表展示
- 可配置评论开关

### 5. 文件上传

- **上传目录**: `src/main/webapp/uploads/`
- **文件类型**: 图片（JPG、PNG、GIF 等）
- **大小限制**: 单文件最大 20MB，请求最大 60MB

---

## API 接口

### 文章接口

| 方法 | 路径 | 参数 | 说明 |
|------|------|------|------|
| GET | `/showArticlelist` | - | 获取文章列表 |
| POST | `/article` | title, markdown, categories, tags, allowComment, thumbnailFile | 创建文章 |
| POST | `/article?action=update` | id, title, markdown, categories, tags, allowComment, thumbnailFile | 更新文章 |
| POST | `/article?action=delete` | id | 删除文章 |
| GET | `/article?action=view` | id | 查看文章详情 |
| GET | `/article?action=edit` | id | 编辑文章（返回编辑页面） |
| POST | `/article?action=search` | id | 根据 ID 搜索文章 |
| POST | `/article?action=searchKeyword` | keyword, category | 关键词搜索 |

### 用户接口

| 方法 | 路径 | 参数 | 说明 |
|------|------|------|------|
| POST | `/login` | username, password | 用户登录 |
| GET | `/logout` | - | 用户登出 |

---

## 开发指南

### 添加新功能

1. **创建实体类**: 在 `com.example.bean` 包下创建
2. **创建 Mapper**: 在 `com.example.dao` 包下创建接口
3. **创建 XML 映射**: 在 `src/main/resources/mapper/` 下创建
4. **创建 Controller**: 在 `com.example.controller` 包下创建
5. **创建 JSP 页面**: 在 `src/main/webapp/` 下创建

### 代码规范

- 使用 Lombok 注解简化 getter/setter
- Controller 使用 Spring MVC 注解
- 数据库操作使用 MyBatis
- 工具类方法使用静态方法
- 异常统一处理

### 日志配置

日志配置文件：`src/main/resources/log4j.properties`

```properties
# 配置根日志级别
log4j.rootLogger=INFO, stdout, file

# 配置控制台输出
log4j.appender.stdout=org.apache.log4j.ConsoleAppender
log4j.appender.stdout.layout=org.apache.log4j.PatternLayout
log4j.appender.stdout.layout.ConversionPattern=%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1}:%L - %m%n

# 配置文件输出
log4j.appender.file=org.apache.log4j.RollingFileAppender
log4j.appender.file.File=logs/application.log
log4j.appender.file.MaxFileSize=10MB
log4j.appender.file.MaxBackupIndex=10
```

---

## 常见问题

### Q1: 启动时报 `ClassNotFoundException: jakarta.servlet.xxx`

**解决方案**: 确保使用 Tomcat 10+ 或其他支持 Jakarta EE 的服务器。Tomcat 9 及以下版本不支持 Jakarta 命名空间。

### Q2: 数据库连接失败

**解决方案**:
1. 检查 MySQL 服务是否启动
2. 验证 `db.properties` 中的配置是否正确
3. 确认数据库 `blog_sys` 已创建
4. 检查用户名和密码是否正确

### Q3: 文件上传失败

**解决方案**:
1. 检查 `uploads` 目录是否存在且有写权限
2. 验证文件大小是否超过限制（20MB）
3. 检查文件类型是否支持

### Q4: Markdown 渲染异常

**解决方案**:
1. 确认 Flexmark 依赖已正确引入
2. 检查 Markdown 内容格式是否正确
3. 查看控制台错误日志

### Q5: 主题样式缺失

**解决方案**: 参考 `src/main/webapp/img/README_COPY_ASSETS.md` 复制主题资源文件。

### Q6: 登录后跳转失败

**解决方案**:
1. 检查 Session 是否正常
2. 验证 AuthFilter 配置是否正确
3. 清除浏览器 Cookie 重试

---

## 贡献指南

欢迎贡献代码！请遵循以下步骤：

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启 Pull Request

### 代码提交规范

- `feat`: 新功能
- `fix`: 修复 Bug
- `docs`: 文档更新
- `style`: 代码格式调整
- `refactor`: 代码重构
- `test`: 测试相关
- `chore`: 构建/工具链相关

---

## 许可证

本项目仅供学习和研究使用。

---

## 联系方式

- **Issue**: [提交问题](https://github.com/Ghib6/javablog/issues)
- **Pull Request**: [贡献代码](https://github.com/Ghib6/javablog/pulls)

---

<div align="center">
  <p>如果这个项目对你有帮助，请给一个 ⭐️ Star 支持一下！</p>
  <p>Made with ❤️ by JavaBlog Team</p>
</div>

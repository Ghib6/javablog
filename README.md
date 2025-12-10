# JavaBlog

一个功能完善的 Java Web 博客系统，支持 Markdown 编辑、文章管理、分类标签、评论等功能。本项目基于 Spring MVC + MyBatis + MySQL 架构，适合作为 Java Web 开发学习和实践的参考项目。

## 📋 项目简介

JavaBlog 是一个现代化的博客系统，提供了完整的博客文章管理功能。系统采用前后端混合架构，后端使用 Spring MVC 框架处理业务逻辑，MyBatis 进行数据持久化，前端使用 JSP + Butterfly 主题提供优雅的用户界面。

### ✨ 主要特性

- **Markdown 支持**：使用 Flexmark 引擎，支持完整的 Markdown 语法编写文章
- **文章管理**：创建、编辑、删除、查看文章，支持文章缩略图上传
- **分类与标签**：灵活的文章分类和标签系统，方便文章组织和检索
- **搜索功能**：支持按关键词和分类搜索文章
- **评论系统**：文章评论功能，可控制是否允许评论
- **用户认证**：管理员登录系统，保护后台管理功能
- **响应式设计**：基于 Butterfly 主题，支持多终端访问
- **文件上传**：支持文章缩略图上传（最大 20MB）

## 🛠️ 技术栈

### 后端技术

| 技术 | 版本 | 说明 |
|------|------|------|
| JDK | 17+ | Java 开发工具包 |
| Spring | 7.0.1 | 核心框架（Spring Context + Spring MVC） |
| MyBatis | 3.5.14 | ORM 持久化框架 |
| MySQL Connector | 9.4.0 | MySQL 数据库驱动 |
| HikariCP | 5.1.0 | 高性能数据库连接池 |
| Lombok | 1.18.38 | 简化 Java Bean 开发 |
| Flexmark | 0.64.0 | Markdown 解析器 |
| Jsoup | 1.16.1 | HTML 解析器 |
| Log4j | 1.2.17 | 日志框架 |

### 前端技术

- **JSP**：JavaServer Pages 模板引擎
- **JSTL**：Jakarta 标准标签库
- **Butterfly 主题**：优雅的博客前端主题
- **JavaScript**：前端交互逻辑

### 构建工具

- **Maven**：项目构建和依赖管理
- **Servlet 5.0**：Jakarta Servlet API（Tomcat 10+）

## 📦 系统架构

```
javablog/
├── src/
│   └── main/
│       ├── java/com/example/
│       │   ├── bean/           # 实体类（Article, User, Comment 等）
│       │   ├── controller/     # Spring MVC 控制器
│       │   ├── dao/            # MyBatis Mapper 接口
│       │   ├── servlet/        # 原生 Servlet（已迁移至 Controller）
│       │   ├── filter/         # 过滤器（认证、数据预处理）
│       │   ├── util/           # 工具类（Markdown 处理、数据库操作等）
│       │   └── config/         # 配置类（数据源等）
│       ├── resources/
│       │   ├── mapper/         # MyBatis XML 映射文件
│       │   ├── mybatis-config.xml  # MyBatis 配置
│       │   ├── spring-mvc.xml      # Spring MVC 配置
│       │   ├── db.properties       # 数据库配置
│       │   └── log4j.properties    # 日志配置
│       └── webapp/
│           ├── WEB-INF/
│           │   └── web.xml     # Web 应用配置
│           ├── css/            # 样式文件
│           ├── js/             # JavaScript 脚本
│           ├── img/            # 图片资源
│           ├── uploads/        # 上传文件目录
│           ├── index.jsp       # 首页
│           ├── login.jsp       # 登录页面
│           ├── addArticle.jsp  # 添加/编辑文章页面
│           ├── articleslist.jsp # 文章列表页面
│           └── viewArticle.jsp # 文章详情页面
└── pom.xml                     # Maven 项目配置
```

## 🚀 环境要求

### 必需环境

- **JDK**: 17 或更高版本
- **Maven**: 3.6 或更高版本
- **MySQL**: 5.7 或更高版本（推荐 8.0+）
- **应用服务器**: Tomcat 10+ 或其他支持 Jakarta Servlet 5.0 的服务器

### 推荐开发工具

- IntelliJ IDEA 2021+ 或 Eclipse 2021+
- MySQL Workbench 或其他数据库管理工具
- Git 版本控制工具

## 📥 安装部署

### 1. 克隆项目

```bash
git clone https://github.com/Ghib6/javablog.git
cd javablog
```

### 2. 数据库配置

#### 2.1 创建数据库

```sql
CREATE DATABASE blog_sys CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE blog_sys;
```

#### 2.2 创建数据表

```sql
-- 管理员用户表
CREATE TABLE admin_user (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 文章表
CREATE TABLE article (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT,
    markdown TEXT,
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    categories VARCHAR(100),
    tags VARCHAR(255),
    allow_comment BOOLEAN DEFAULT TRUE,
    thumbnail VARCHAR(255)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 评论表
CREATE TABLE comment (
    id INT AUTO_INCREMENT PRIMARY KEY,
    article_id INT NOT NULL,
    author VARCHAR(50) NOT NULL,
    email VARCHAR(100),
    content TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (article_id) REFERENCES article(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

#### 2.3 插入测试数据

```sql
-- 插入管理员账号（密码：admin123，实际使用时请加密）
INSERT INTO admin_user (username, password) VALUES ('admin', 'admin123');

-- 插入示例文章
INSERT INTO article (title, markdown, categories, tags, allow_comment) 
VALUES ('欢迎使用 JavaBlog', '# 欢迎\n\n这是你的第一篇博客文章！', 'Java', 'Java,Web', TRUE);
```

#### 2.4 修改数据库配置

编辑 `src/main/resources/db.properties` 文件，配置你的数据库连接信息：

```properties
mysql.driver=com.mysql.cj.jdbc.Driver
mysql.url=jdbc:mysql://localhost:3306/blog_sys?useSSL=false&serverTimezone=UTC
mysql.username=root
mysql.password=你的数据库密码
```

### 3. 构建项目

```bash
# 清理并打包项目
mvn clean package

# 或者跳过测试打包
mvn clean package -DskipTests
```

构建成功后，会在 `target` 目录下生成 `9.23javaee.war` 文件。

### 4. 部署到服务器

#### 方式一：Tomcat 部署

1. 将 `target/9.23javaee.war` 复制到 Tomcat 的 `webapps` 目录
2. 启动 Tomcat 服务器
3. 访问 `http://localhost:8080/9.23javaee`

#### 方式二：IDE 集成开发

**IntelliJ IDEA**：
1. 配置 Tomcat 服务器：Run -> Edit Configurations -> Add New Configuration -> Tomcat Server -> Local
2. 在 Deployment 选项卡中添加 war 包或 exploded 目录
3. 点击运行按钮启动应用

**Eclipse**：
1. 右键项目 -> Run As -> Run on Server
2. 选择 Tomcat 服务器并完成配置
3. 启动服务器

### 5. 访问应用

- **首页**：`http://localhost:8080/9.23javaee/index`
- **登录页**：`http://localhost:8080/9.23javaee/login`
- **文章列表**（需登录）：`http://localhost:8080/9.23javaee/showArticlelist`

**默认管理员账号**：
- 用户名：`admin`
- 密码：`admin123`（请在生产环境中修改）

## 📖 使用说明

### 文章管理

#### 创建文章
1. 登录后台管理系统
2. 点击"添加文章"按钮
3. 填写文章标题、Markdown 内容、分类、标签
4. 可选：上传文章缩略图（支持 JPG、PNG 等格式，最大 20MB）
5. 选择是否允许评论
6. 点击"发布"提交文章

#### 编辑文章
1. 在文章列表中找到要编辑的文章
2. 点击"编辑"按钮
3. 修改文章内容
4. 保存更新

#### 删除文章
1. 在文章列表中找到要删除的文章
2. 点击"删除"按钮
3. 确认删除操作

### 搜索功能

支持两种搜索方式：
- **关键词搜索**：在标题和内容中搜索关键词
- **分类筛选**：按文章分类筛选

### 评论管理

- 每篇文章可以控制是否允许评论
- 访客可以在文章详情页留言
- 评论会显示作者、时间和内容

## 🔌 API 接口说明

### 文章相关接口

| 接口 | 方法 | 路径 | 说明 |
|------|------|------|------|
| 首页 | GET | `/index` | 文章列表首页，支持搜索和分类筛选 |
| 查看文章 | GET | `/article?action=view&id={id}` | 查看文章详情 |
| 编辑文章页 | GET | `/article?action=edit&id={id}` | 获取编辑页面 |
| 创建文章 | POST | `/article` | 创建新文章 |
| 更新文章 | POST | `/article?action=update` | 更新文章内容 |
| 删除文章 | POST | `/article?action=delete` | 删除指定文章 |
| 搜索文章 | POST | `/article?action=searchKeyword` | 关键词搜索 |
| 文章列表 | GET | `/showArticlelist` | 后台文章管理列表 |

### 用户认证接口

| 接口 | 方法 | 路径 | 说明 |
|------|------|------|------|
| 登录页面 | GET | `/login` | 显示登录页面 |
| 登录验证 | POST | `/login` | 验证用户名密码 |

### 请求参数示例

**创建文章**：
```
POST /article
Content-Type: multipart/form-data

title: 文章标题
markdown: Markdown 内容
categories: Java
tags: Java,Spring,MyBatis
allowComment: true
thumbnailFile: [文件]
```

**搜索文章**：
```
POST /article?action=searchKeyword
Content-Type: application/x-www-form-urlencoded

keyword: Spring
category: Java
```

## 🔧 开发指南

### 本地开发环境设置

1. **导入项目**
   ```bash
   git clone https://github.com/Ghib6/javablog.git
   cd javablog
   mvn clean install
   ```

2. **配置数据库**
   - 按照"安装部署"章节配置数据库
   - 修改 `db.properties` 文件

3. **运行项目**
   - 使用 IDE 配置 Tomcat 服务器
   - 或使用 Maven Tomcat 插件运行

### 代码规范

- 使用 Lombok 简化实体类代码
- Controller 层负责请求处理和路由
- Service/Util 层封装业务逻辑
- DAO/Mapper 层负责数据访问
- 遵循 RESTful 设计原则
- 使用有意义的变量和方法命名

### 扩展开发

#### 添加新功能

1. 在 `bean` 包中创建实体类
2. 在 `dao` 包中创建 Mapper 接口
3. 在 `resources/mapper` 中创建 XML 映射文件
4. 在 `util` 包中实现业务逻辑
5. 在 `controller` 包中创建控制器
6. 创建对应的 JSP 页面

#### 自定义主题

1. 修改 `webapp/css` 目录下的样式文件
2. 调整 JSP 页面布局和结构
3. 替换 `img` 目录中的图片资源

### 测试

项目包含基础测试类，可以扩展：

```bash
# 运行所有测试
mvn test

# 运行特定测试类
mvn test -Dtest=ArticleTest
```

## 🐛 常见问题

### 1. 数据库连接失败

**问题**：启动时报 `Could not create connection to database server`

**解决方案**：
- 检查 MySQL 服务是否启动
- 确认 `db.properties` 中的连接信息是否正确
- 检查数据库用户权限
- 确认防火墙未阻止 3306 端口

### 2. 404 页面未找到

**问题**：访问页面返回 404 错误

**解决方案**：
- 确认应用已正确部署到 Tomcat
- 检查访问路径是否包含项目上下文路径（如 `/9.23javaee`）
- 查看 Tomcat 日志确认应用是否启动成功

### 3. Markdown 渲染问题

**问题**：Markdown 内容未正确渲染为 HTML

**解决方案**：
- 确认 Flexmark 依赖已正确引入
- 检查 `MarkdownService` 类是否正常工作
- 查看浏览器控制台是否有 JavaScript 错误

### 4. 文件上传失败

**问题**：上传缩略图时失败

**解决方案**：
- 确认 `uploads` 目录存在且有写权限
- 检查文件大小是否超过 20MB 限制
- 查看 `web.xml` 中的 multipart 配置
- 检查磁盘空间是否充足

### 5. 部署到生产环境注意事项

- 修改默认管理员密码
- 使用加密算法存储用户密码（如 BCrypt）
- 配置 HTTPS 加密传输
- 定期备份数据库
- 配置日志轮转和监控
- 限制文件上传大小和类型
- 添加 SQL 注入和 XSS 防护

## 📝 项目特色

### Markdown 编辑器
- 支持标准 Markdown 语法
- 实时预览（可扩展）
- 代码高亮支持

### Butterfly 主题集成
- 优雅的博客界面设计
- 响应式布局，适配各种设备
- 丰富的视觉效果

### 数据库连接池
- 使用 HikariCP 高性能连接池
- 优化数据库访问性能
- 支持连接复用和管理

## 📄 许可证

本项目仅供学习和参考使用。

## 🤝 贡献

欢迎提交 Issue 和 Pull Request 来改进这个项目！

### 贡献步骤

1. Fork 本仓库
2. 创建你的特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交你的更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 打开一个 Pull Request

## 📮 联系方式

如有问题或建议，欢迎通过 GitHub Issues 联系。

## 🙏 致谢

- Spring Framework 团队
- MyBatis 团队  
- Butterfly 主题作者
- 所有开源项目贡献者

---

**注意**：本项目作为学习示例，生产环境使用前请加强安全性配置，包括但不限于：
- 密码加密存储
- SQL 注入防护
- XSS 攻击防护
- CSRF 令牌验证
- 完善的权限控制
- 日志审计功能

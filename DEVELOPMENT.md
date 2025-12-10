# JavaBlog 开发指南

本文档提供了 JavaBlog 项目的详细开发指南，帮助开发者快速上手项目开发和维护。

## 目录

- [开发环境搭建](#开发环境搭建)
- [项目结构详解](#项目结构详解)
- [核心技术解析](#核心技术解析)
- [开发流程](#开发流程)
- [代码规范](#代码规范)
- [调试技巧](#调试技巧)
- [常见开发任务](#常见开发任务)

## 开发环境搭建

### 1. 安装必需软件

#### JDK 17+
```bash
# 验证 Java 版本
java -version

# 应该显示 Java 17 或更高版本
```

#### Maven 3.6+
```bash
# 验证 Maven 版本
mvn -version
```

#### MySQL 8.0+
```bash
# 启动 MySQL 服务
# Windows
net start MySQL80

# Linux/Mac
sudo systemctl start mysql
```

### 2. IDE 配置

#### IntelliJ IDEA（推荐）

1. **导入项目**:
   - File → Open → 选择项目根目录
   - 选择 "Import as Maven project"

2. **配置 Tomcat**:
   - Run → Edit Configurations
   - 添加 Tomcat Server → Local
   - 配置 Tomcat 安装路径
   - 在 Deployment 标签添加 artifact: `9.23javaee:war exploded`
   - Application context: `/9.23javaee`

3. **启用 Lombok**:
   - File → Settings → Plugins
   - 搜索并安装 "Lombok" 插件
   - Settings → Build → Compiler → Annotation Processors
   - 勾选 "Enable annotation processing"

#### Eclipse

1. **导入项目**:
   - File → Import → Maven → Existing Maven Projects
   - 选择项目根目录

2. **配置 Server**:
   - Window → Preferences → Server → Runtime Environments
   - 添加 Apache Tomcat 10.x

3. **安装 Lombok**:
   - 下载 lombok.jar
   - 双击运行安装程序
   - 选择 Eclipse 安装路径

### 3. 数据库初始化

```bash
# 登录 MySQL
mysql -u root -p

# 执行初始化脚本
source /path/to/init.sql

# 或者直接执行 SQL 语句（见 README.md）
```

### 4. 配置文件

编辑 `src/main/resources/db.properties`:
```properties
mysql.driver=com.mysql.cj.jdbc.Driver
mysql.url=jdbc:mysql://localhost:3306/blog_sys?useSSL=false&serverTimezone=UTC
mysql.username=root
mysql.password=your_password
```

### 5. 启动项目

```bash
# 方式一：使用 IDE 运行
# 点击 Run 按钮启动 Tomcat

# 方式二：使用 Maven
mvn clean package
# 将生成的 WAR 包部署到 Tomcat

# 方式三：使用 Maven Tomcat 插件（需要配置插件）
mvn tomcat7:run
```

访问: `http://localhost:8080/9.23javaee/index`

---

## 项目结构详解

### Maven 项目结构

```
javablog/
├── src/
│   ├── main/
│   │   ├── java/                    # Java 源代码
│   │   │   └── com/example/
│   │   │       ├── bean/            # 实体类（POJO）
│   │   │       ├── controller/      # Spring MVC 控制器
│   │   │       ├── dao/             # 数据访问接口
│   │   │       ├── servlet/         # 传统 Servlet
│   │   │       ├── filter/          # 过滤器
│   │   │       ├── util/            # 工具类
│   │   │       ├── config/          # 配置类
│   │   │       └── text/            # 测试类
│   │   ├── resources/               # 资源文件
│   │   │   ├── mapper/              # MyBatis XML 映射
│   │   │   ├── mybatis-config.xml   # MyBatis 配置
│   │   │   ├── spring-mvc.xml       # Spring MVC 配置
│   │   │   ├── db.properties        # 数据库配置
│   │   │   └── log4j.properties     # 日志配置
│   │   └── webapp/                  # Web 资源
│   │       ├── WEB-INF/
│   │       │   └── web.xml          # Web 应用配置
│   │       ├── css/                 # 样式文件
│   │       ├── images/              # 图片资源
│   │       ├── img/                 # 主题图片
│   │       └── *.jsp                # JSP 页面
│   └── test/                        # 测试代码
├── target/                          # 编译输出（不提交到版本控制）
├── pom.xml                          # Maven 配置
└── README.md                        # 项目说明
```

### 包结构说明

#### com.example.bean
实体类包，使用 Lombok 简化代码：
- `Article.java` - 文章实体
- `AdminUser.java` - 管理员实体
- `User.java` - 用户实体
- `Comment.java` - 评论实体

#### com.example.controller
Spring MVC 控制器包：
- `IndexController.java` - 首页控制器
- `LoginController.java` - 登录控制器
- `ArticleController.java` - 文章管理控制器

#### com.example.dao
MyBatis Mapper 接口：
- `ArticleMapper.java` - 文章数据访问
- `AdminUserMapper.java` - 管理员数据访问

#### com.example.util
工具类包：
- `ArticleUtil.java` - 文章相关业务逻辑
- `AdminUserUtil.java` - 用户相关业务逻辑
- `MarkdownService.java` - Markdown 转换服务
- `MyBatisUtils.java` - MyBatis 工具类
- `Util.java` - 通用工具类

#### com.example.filter
过滤器包：
- `AuthFilter.java` - 认证过滤器
- `IndexDataFilter.java` - 首页数据预处理过滤器

---

## 核心技术解析

### Spring MVC

#### 配置文件: spring-mvc.xml

```xml
<!-- 启用注解驱动 -->
<mvc:annotation-driven/>

<!-- 组件扫描 -->
<context:component-scan base-package="com.example.controller"/>

<!-- 视图解析器 -->
<bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
    <property name="prefix" value="/"/>
    <property name="suffix" value=".jsp"/>
</bean>
```

#### 控制器示例

```java
@Controller
public class ArticleController {
    
    @GetMapping("/article")
    public String viewArticle(@RequestParam("id") Integer id, 
                             HttpServletRequest request) {
        Article article = articleUtil.getArticleById(id);
        request.setAttribute("article", article);
        return "forward:viewArticle.jsp";
    }
    
    @PostMapping("/article")
    public Object createArticle(HttpServletRequest request) {
        // 处理文章创建
        return "redirect:/showArticlelist";
    }
}
```

### MyBatis

#### 配置文件: mybatis-config.xml

```xml
<configuration>
    <settings>
        <setting name="mapUnderscoreToCamelCase" value="true"/>
        <setting name="logImpl" value="LOG4J"/>
    </settings>
    
    <mappers>
        <mapper resource="mapper/ArticleMapper.xml"/>
    </mappers>
</configuration>
```

#### Mapper 接口

```java
public interface ArticleMapper {
    List<Article> selectAll();
    Article selectById(Integer id);
    int insert(Article article);
    int update(Article article);
    int delete(Integer id);
}
```

#### XML 映射文件

```xml
<mapper namespace="com.example.dao.ArticleMapper">
    <select id="selectAll" resultType="Article">
        SELECT * FROM article ORDER BY created DESC
    </select>
    
    <insert id="insert" parameterType="Article">
        INSERT INTO article (title, content, markdown, created, categories, tags)
        VALUES (#{title}, #{content}, #{markdown}, #{created}, #{categories}, #{tags})
    </insert>
</mapper>
```

### HikariCP 连接池

```java
public class DataSourceProvider {
    private static HikariDataSource dataSource;
    
    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(properties.getProperty("mysql.url"));
        config.setUsername(properties.getProperty("mysql.username"));
        config.setPassword(properties.getProperty("mysql.password"));
        config.setMaximumPoolSize(10);
        dataSource = new HikariDataSource(config);
    }
}
```

### Markdown 处理

```java
public class MarkdownService {
    private final Parser parser = Parser.builder().build();
    private final HtmlRenderer renderer = HtmlRenderer.builder().build();
    
    public String convertToHtml(String markdown) {
        Node document = parser.parse(markdown);
        return renderer.render(document);
    }
}
```

---

## 开发流程

### 添加新功能的标准流程

#### 1. 定义实体类

```java
@Getter
@Setter
public class Category {
    private Integer id;
    private String name;
    private String description;
    private Date created;
}
```

#### 2. 创建 Mapper 接口

```java
public interface CategoryMapper {
    List<Category> selectAll();
    Category selectById(Integer id);
    int insert(Category category);
}
```

#### 3. 编写 XML 映射

```xml
<mapper namespace="com.example.dao.CategoryMapper">
    <select id="selectAll" resultType="Category">
        SELECT * FROM category
    </select>
</mapper>
```

#### 4. 创建工具类/服务类

```java
public class CategoryUtil {
    private final CategoryMapper mapper;
    
    public List<Category> getAllCategories() {
        try (SqlSession session = MyBatisUtils.getSqlSession()) {
            mapper = session.getMapper(CategoryMapper.class);
            return mapper.selectAll();
        }
    }
}
```

#### 5. 创建控制器

```java
@Controller
public class CategoryController {
    private final CategoryUtil util = new CategoryUtil();
    
    @GetMapping("/categories")
    public String listCategories(HttpServletRequest request) {
        List<Category> categories = util.getAllCategories();
        request.setAttribute("categories", categories);
        return "forward:categories.jsp";
    }
}
```

#### 6. 创建 JSP 页面

```jsp
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:forEach items="${categories}" var="category">
    <div>${category.name}</div>
</c:forEach>
```

#### 7. 测试功能

- 单元测试
- 集成测试
- 手动测试

---

## 代码规范

### Java 代码规范

1. **命名规范**:
   - 类名: 大驼峰（PascalCase）- `ArticleController`
   - 方法名: 小驼峰（camelCase）- `getArticleById`
   - 常量: 全大写下划线分隔 - `MAX_SIZE`
   - 包名: 全小写 - `com.example.util`

2. **注释规范**:
```java
/**
 * 获取文章详情
 * @param id 文章ID
 * @return 文章对象，不存在返回null
 */
public Article getArticleById(Integer id) {
    // 实现代码
}
```

3. **异常处理**:
```java
try {
    // 业务代码
} catch (Exception e) {
    log.error("错误信息", e);
    throw new RuntimeException("操作失败", e);
}
```

### SQL 规范

1. **使用参数化查询**:
```xml
<!-- 正确 -->
<select id="selectById">
    SELECT * FROM article WHERE id = #{id}
</select>

<!-- 错误：容易 SQL 注入 -->
<select id="selectById">
    SELECT * FROM article WHERE id = ${id}
</select>
```

2. **字段映射**:
```xml
<resultMap id="ArticleResultMap" type="Article">
    <id property="id" column="id"/>
    <result property="allowComment" column="allow_comment"/>
</resultMap>
```

---

## 调试技巧

### 1. 日志调试

配置 `log4j.properties`:
```properties
log4j.rootLogger=DEBUG, stdout

log4j.logger.com.example=DEBUG
log4j.logger.org.springframework=INFO
log4j.logger.org.mybatis=DEBUG
```

在代码中使用:
```java
import org.apache.log4j.Logger;

public class ArticleUtil {
    private static final Logger log = Logger.getLogger(ArticleUtil.class);
    
    public Article getArticleById(Integer id) {
        log.debug("查询文章, ID: " + id);
        // ...
    }
}
```

### 2. MyBatis SQL 调试

查看生成的 SQL:
```properties
log4j.logger.com.example.dao=DEBUG
```

### 3. 断点调试

- 在 IDE 中设置断点
- 以 Debug 模式启动应用
- 逐步执行代码

### 4. 浏览器开发者工具

- F12 打开开发者工具
- Network 标签查看请求响应
- Console 标签查看 JavaScript 错误

---

## 常见开发任务

### 添加新的数据表

1. 在数据库中创建表
2. 创建对应的实体类
3. 创建 Mapper 接口和 XML 文件
4. 在 mybatis-config.xml 中注册 Mapper
5. 创建工具类处理业务逻辑
6. 创建控制器处理请求

### 修改已有功能

1. 找到对应的控制器
2. 查看调用的工具类方法
3. 检查 Mapper 接口和 SQL
4. 修改代码
5. 测试修改

### 优化查询性能

1. 使用 EXPLAIN 分析 SQL
2. 添加适当的索引
3. 优化 SQL 查询语句
4. 使用缓存（如需要）

### 添加前端页面

1. 在 webapp 目录创建 JSP 文件
2. 使用 JSTL 标签
3. 引入 CSS 和 JavaScript
4. 创建对应的控制器方法

---

## 部署上线

### 生产环境配置

1. **修改数据库配置**:
```properties
mysql.url=jdbc:mysql://production-host:3306/blog_sys?useSSL=true
mysql.username=prod_user
mysql.password=strong_password
```

2. **日志级别调整**:
```properties
log4j.rootLogger=INFO, file
```

3. **构建生产包**:
```bash
mvn clean package -P production
```

4. **部署到 Tomcat**:
- 将 WAR 包复制到 Tomcat webapps 目录
- 配置 Tomcat 的 server.xml
- 启动 Tomcat

### 性能优化建议

1. 启用 Gzip 压缩
2. 配置静态资源缓存
3. 优化数据库连接池
4. 使用 CDN 加载静态资源
5. 启用 HTTP/2

---

## 疑难问题排查

### 项目无法启动

1. 检查 Tomcat 日志
2. 检查数据库连接
3. 检查端口占用
4. 验证依赖完整性

### 页面 404 错误

1. 检查 URL 映射
2. 检查控制器注解
3. 检查 JSP 文件路径
4. 检查 web.xml 配置

### 数据库操作失败

1. 检查 SQL 语法
2. 检查表结构
3. 查看 MyBatis 日志
4. 验证参数类型

---

## 学习资源

### 官方文档

- [Spring Framework](https://docs.spring.io/spring-framework/docs/current/reference/html/)
- [MyBatis](https://mybatis.org/mybatis-3/)
- [HikariCP](https://github.com/brettwooldridge/HikariCP)

### 推荐书籍

- 《Spring 实战》
- 《深入浅出 MyBatis》
- 《Java Web 开发实战》

---

最后更新: 2024-12-10

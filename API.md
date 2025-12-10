# JavaBlog API 文档

本文档详细说明了 JavaBlog 系统的所有 API 接口和页面路由。

## 目录

- [前台页面](#前台页面)
- [后台管理](#后台管理)
- [文章 API](#文章-api)
- [用户认证](#用户认证)
- [数据格式](#数据格式)

## 前台页面

### 首页

**路由**: `GET /index`

**描述**: 展示博客首页，显示所有文章列表

**参数**:
- `keyword` (可选): 搜索关键词
- `category` (可选): 文章分类

**示例**:
```
GET /index
GET /index?keyword=Java
GET /index?category=技术
GET /index?keyword=Spring&category=后端
```

**返回**: 渲染 `index.jsp` 页面

---

### 文章详情

**路由**: `GET /article?action=view&id={id}`

**描述**: 查看指定文章的详细内容

**参数**:
- `id` (必需): 文章 ID

**示例**:
```
GET /article?action=view&id=1
```

**返回**: 渲染 `viewArticle.jsp` 页面，包含文章内容和评论

---

## 后台管理

### 登录页面

**路由**: `GET /login`

**描述**: 显示管理员登录页面

**返回**: 渲染 `login.jsp` 页面

---

### 登录认证

**路由**: `POST /login`

**描述**: 管理员登录认证

**参数**:
- `username` (必需): 用户名
- `password` (必需): 密码

**请求体** (application/x-www-form-urlencoded):
```
username=admin&password=admin
```

**成功响应**: 重定向到 `/showArticlelist`

**失败响应**: 返回 `login.jsp` 页面，显示错误信息

---

### 文章列表

**路由**: `GET /showArticlelist`

**描述**: 显示所有文章列表（管理后台）

**返回**: 渲染 `articleslist.jsp` 页面

---

## 文章 API

### 创建文章

**路由**: `POST /article`

**描述**: 创建新文章

**参数**:
- `title` (必需): 文章标题
- `markdown` (必需): Markdown 格式的文章内容
- `categories` (可选): 文章分类
- `tags` (可选): 文章标签
- `allowComment` (可选): 是否允许评论 (true/false)
- `thumbnailFile` (可选): 缩略图文件

**请求类型**: `multipart/form-data`

**成功响应**: 重定向到 `/showArticlelist`

**失败响应**: HTTP 500，返回错误信息

**示例**:
```
POST /article
Content-Type: multipart/form-data

title=Spring Boot 入门
markdown=# Spring Boot 简介...
categories=技术
tags=Java,Spring
allowComment=true
thumbnailFile=<binary>
```

---

### 更新文章

**路由**: `POST /article?action=update`

**描述**: 更新现有文章

**参数**:
- `id` (必需): 文章 ID
- `title` (必需): 文章标题
- `markdown` (必需): Markdown 格式的文章内容
- `categories` (可选): 文章分类
- `tags` (可选): 文章标签
- `allowComment` (可选): 是否允许评论
- `thumbnailFile` (可选): 新的缩略图文件

**请求类型**: `multipart/form-data`

**成功响应**: 重定向到 `/showArticlelist`

**失败响应**: 
- HTTP 400: 未指定 ID
- HTTP 404: 未找到要修改的文章

**示例**:
```
POST /article?action=update
Content-Type: multipart/form-data

id=1
title=Spring Boot 入门（更新版）
markdown=# Spring Boot 简介（更新）...
```

---

### 删除文章

**路由**: `POST /article?action=delete`

**描述**: 删除指定文章

**参数**:
- `id` (必需): 文章 ID

**成功响应**: 重定向到 `/showArticlelist`

**失败响应**: 
- HTTP 400: 未指定 ID
- HTTP 404: 未删除任何文章

**示例**:
```
POST /article?action=delete
id=1
```

---

### 编辑文章页面

**路由**: `GET /article?action=edit&id={id}`

**描述**: 显示文章编辑页面

**参数**:
- `id` (必需): 文章 ID

**成功响应**: 渲染 `addArticle.jsp` 页面，包含文章数据

**失败响应**: 
- HTTP 400: 未指定 ID
- HTTP 404: 未找到要修改的文章

**示例**:
```
GET /article?action=edit&id=1
```

---

### 按 ID 查询文章

**路由**: `POST /article?action=search`

**描述**: 根据文章 ID 查询文章信息

**参数**:
- `id` (必需): 文章 ID

**成功响应**: HTTP 200，返回文章信息

**失败响应**: 
- HTTP 400: 未指定 ID
- HTTP 404: 未找到该 ID 的文章

**示例**:
```
POST /article?action=search
id=1
```

**响应示例**:
```
查询结果: Article{id=1, title='Spring Boot 入门', created=2024-01-01, ...}
```

---

### 关键词搜索文章

**路由**: `POST /article?action=searchKeyword`

**描述**: 根据关键词和分类搜索文章

**参数**:
- `keyword` (可选): 搜索关键词
- `category` (可选): 文章分类

**成功响应**: 渲染 `articleslist.jsp` 页面，显示搜索结果

**示例**:
```
POST /article?action=searchKeyword
keyword=Spring
category=技术
```

---

## 用户认证

### 认证机制

系统使用基于 Session 的认证机制：

1. 用户通过 `/login` 提交用户名和密码
2. 服务器验证凭据，成功后在 Session 中存储用户信息
3. 后续请求通过 `AuthFilter` 验证 Session 中的用户信息
4. 未认证用户访问受保护页面会被重定向到登录页

### 受保护的路由

以下路由需要管理员登录后才能访问：

- `/showArticlelist` - 文章列表管理
- `/article` (POST) - 创建/更新/删除文章
- `/article?action=edit` - 编辑文章页面

### 公开路由

以下路由无需登录即可访问：

- `/index` - 博客首页
- `/article?action=view` - 查看文章
- `/login` - 登录页面

---

## 数据格式

### Article (文章)

```json
{
  "id": 1,
  "title": "文章标题",
  "content": "HTML 格式的文章内容",
  "markdown": "Markdown 格式的原始内容",
  "created": "2024-01-01T10:00:00",
  "modified": "2024-01-02T15:30:00",
  "categories": "技术",
  "tags": "Java,Spring",
  "allowComment": true,
  "thumbnail": "/images/thumb1.jpg"
}
```

### Comment (评论)

```json
{
  "id": 1,
  "articleId": 1,
  "author": "评论者",
  "email": "user@example.com",
  "content": "评论内容",
  "created": "2024-01-01T10:00:00"
}
```

### AdminUser (管理员)

```json
{
  "id": 1,
  "username": "admin",
  "password": "加密后的密码",
  "created": "2024-01-01T10:00:00"
}
```

---

## 文件上传

### 缩略图上传

文章缩略图上传使用 `multipart/form-data` 格式：

**限制**:
- 最大文件大小: 20MB
- 支持的格式: JPG, PNG, GIF, WebP
- 文件保存位置: `src/main/webapp/images/`

**上传示例**:
```html
<form action="/article" method="post" enctype="multipart/form-data">
  <input type="text" name="title" value="文章标题">
  <textarea name="markdown">文章内容</textarea>
  <input type="file" name="thumbnailFile" accept="image/*">
  <button type="submit">提交</button>
</form>
```

---

## 错误码

| HTTP 状态码 | 说明 |
|------------|------|
| 200 | 请求成功 |
| 302 | 重定向（操作成功） |
| 400 | 请求参数错误（如缺少必需参数） |
| 404 | 资源未找到 |
| 500 | 服务器内部错误 |

---

## 使用示例

### 示例 1: 创建一篇新文章

```bash
curl -X POST http://localhost:8080/9.23javaee/article \
  -H "Content-Type: multipart/form-data" \
  -F "title=Spring Boot 入门教程" \
  -F "markdown=# Spring Boot 简介\n\nSpring Boot 是..." \
  -F "categories=技术" \
  -F "tags=Java,Spring Boot" \
  -F "allowComment=true" \
  -F "thumbnailFile=@/path/to/image.jpg"
```

### 示例 2: 搜索文章

```bash
curl -X GET "http://localhost:8080/9.23javaee/index?keyword=Spring&category=技术"
```

### 示例 3: 删除文章

```bash
curl -X POST "http://localhost:8080/9.23javaee/article?action=delete" \
  -d "id=1"
```

---

## 注意事项

1. **认证**: 所有管理操作需要先登录
2. **文件上传**: 确保服务器有足够的存储空间
3. **并发**: 系统使用数据库事务保证数据一致性
4. **安全**: 建议在生产环境中使用 HTTPS
5. **性能**: 大量数据时考虑添加分页功能

---

## 扩展建议

为了更好地使用和扩展本系统，建议：

1. 添加 RESTful API 支持，返回 JSON 格式数据
2. 实现分页功能，避免一次加载过多数据
3. 添加图片验证码防止暴力登录
4. 实现评论功能的完整 CRUD 操作
5. 添加文章草稿功能
6. 实现标签云和分类统计
7. 支持文章定时发布
8. 添加文章访问量统计

---

最后更新: 2024-12-10

# Hexo 主题资源复制指南

本文档说明如何将 Hexo Butterfly 主题的静态资源（图片、字体、CSS 等）复制到 JavaBlog 项目中。

---

## 📋 概述

JavaBlog 项目采用了 Hexo Butterfly 主题的前端样式，需要将主题的静态资源文件复制到项目的 `webapp` 目录下才能正常显示样式和图片。

---

## 🎯 需要复制的资源

### 必需资源
- **图片文件** (`img/` 目录)
  - `favicon.ico` - 网站图标
  - `404.jpg` - 404 错误页面图片
  - `error-page.png` - 错误页面图片
  - `butterfly-icon.png` - Butterfly 主题图标
  - `friend_404.gif` - 友链 404 图片
  - `wechat.jpg` - 微信二维码等

- **字体文件** (`fonts/` 目录)
  - Web 字体文件（如有）

- **其他静态资源**
  - CSS 文件（通常已在项目中）
  - JavaScript 文件（通常已在项目中）

---

## 💻 复制方法

### 方法一：PowerShell 脚本（Windows）

在 PowerShell 终端中执行以下命令：

```powershell
# ============================================================
# Hexo Butterfly 主题资源复制脚本
# ============================================================

# 1. 设定源目录与目标目录（请根据实际路径调整）
$hexoThemePath = "C:\BOKE\Blog\themes\hexo-theme-butterfly\source"
$javaBlogPath = "D:\javadaima\blog1122\src\main\webapp"

# 2. 复制图片目录
$srcImg = Join-Path $hexoThemePath "img"
$dstImg = Join-Path $javaBlogPath "img"

If (-Not (Test-Path $dstImg)) { 
    New-Item -ItemType Directory -Path $dstImg -Force 
    Write-Host "创建目录: $dstImg" -ForegroundColor Green
}

Get-ChildItem -Path $srcImg -File | ForEach-Object { 
    Copy-Item $_.FullName -Destination $dstImg -Force 
    Write-Host "复制文件: $($_.Name)" -ForegroundColor Cyan
}

# 3. 复制 favicon 到 webapp 根目录
$faviconSrc = Join-Path $srcImg "favicon.ico"
$faviconDst = Join-Path $javaBlogPath "favicon.ico"

If (Test-Path $faviconSrc) {
    Copy-Item $faviconSrc -Destination $faviconDst -Force
    Write-Host "复制 favicon.ico 到根目录" -ForegroundColor Green
}

# 4. 复制字体目录（如果存在）
$srcFonts = Join-Path $hexoThemePath "fonts"
$dstFonts = Join-Path $javaBlogPath "fonts"

If (Test-Path $srcFonts) {
    If (-Not (Test-Path $dstFonts)) { 
        New-Item -ItemType Directory -Path $dstFonts -Force 
        Write-Host "创建目录: $dstFonts" -ForegroundColor Green
    }
    
    Copy-Item -Path "$srcFonts\*" -Destination $dstFonts -Recurse -Force
    Write-Host "复制字体文件完成" -ForegroundColor Green
}

Write-Host "`n资源复制完成！" -ForegroundColor Yellow
```

### 方法二：Bash 脚本（Linux/macOS）

在终端中执行以下命令：

```bash
#!/bin/bash

# ============================================================
# Hexo Butterfly 主题资源复制脚本
# ============================================================

# 1. 设定源目录与目标目录（请根据实际路径调整）
HEXO_THEME_PATH="/path/to/Blog/themes/hexo-theme-butterfly/source"
JAVABLOG_PATH="/path/to/javablog/src/main/webapp"

# 2. 复制图片目录
SRC_IMG="$HEXO_THEME_PATH/img"
DST_IMG="$JAVABLOG_PATH/img"

mkdir -p "$DST_IMG"
cp -rf "$SRC_IMG"/* "$DST_IMG/"
echo "图片文件复制完成"

# 3. 复制 favicon 到 webapp 根目录
if [ -f "$SRC_IMG/favicon.ico" ]; then
    cp -f "$SRC_IMG/favicon.ico" "$JAVABLOG_PATH/favicon.ico"
    echo "favicon.ico 复制完成"
fi

# 4. 复制字体目录（如果存在）
if [ -d "$HEXO_THEME_PATH/fonts" ]; then
    mkdir -p "$JAVABLOG_PATH/fonts"
    cp -rf "$HEXO_THEME_PATH/fonts"/* "$JAVABLOG_PATH/fonts/"
    echo "字体文件复制完成"
fi

echo "所有资源复制完成！"
```

### 方法三：手动复制

1. 打开 Hexo 主题目录：`themes/hexo-theme-butterfly/source/`
2. 将 `img/` 目录下的所有文件复制到项目的 `src/main/webapp/img/`
3. 将 `img/favicon.ico` 复制到 `src/main/webapp/favicon.ico`
4. 如有 `fonts/` 目录，复制到 `src/main/webapp/fonts/`

---

## ✅ 验证复制结果

### 1. 检查文件列表

复制完成后，确认以下文件存在：

```
src/main/webapp/
├── img/
│   ├── favicon.ico
│   ├── 404.jpg
│   ├── error-page.png
│   ├── butterfly-icon.png
│   ├── friend_404.gif
│   ├── wechat.jpg
│   └── ... (其他图片文件)
├── fonts/
│   └── ... (字体文件，如有)
└── favicon.ico
```

### 2. 运行项目测试

```bash
# 构建项目
mvn clean package

# 部署到 Tomcat 并启动
# 在浏览器中访问：http://localhost:8080/9.23javaee/
```

### 3. 检查浏览器控制台

1. 打开浏览器开发者工具（F12）
2. 切换到 **Console** 标签
3. 切换到 **Network** 标签
4. 刷新页面
5. 查看是否有 404 错误或资源加载失败

常见的资源路径问题：
```
❌ 404 Not Found: /img/favicon.ico
❌ 404 Not Found: /fonts/fontawesome-webfont.woff2
❌ 404 Not Found: /css/butterfly.css
```

如果发现 404 错误，请检查：
- 文件是否正确复制到目标目录
- 文件名大小写是否匹配
- 路径配置是否正确

---

## 🔧 常见问题

### Q1: PowerShell 脚本执行被禁止

**错误信息**:
```
无法加载文件，因为在此系统上禁止运行脚本
```

**解决方案**:
```powershell
# 以管理员身份运行 PowerShell
Set-ExecutionPolicy -ExecutionPolicy RemoteSigned -Scope CurrentUser

# 或者临时允许
powershell -ExecutionPolicy Bypass -File script.ps1
```

### Q2: 找不到 Hexo 主题目录

**解决方案**:
1. 确认 Hexo 博客是否已安装
2. 检查主题是否为 Butterfly
3. 验证主题目录路径：`your-blog/themes/hexo-theme-butterfly/`

### Q3: 图片路径错误

**解决方案**:
1. 检查 CSS 中的图片引用路径
2. 确保使用相对路径或正确的绝对路径
3. 修改 `butterfly.css` 中的路径配置：

```css
/* 修改前 */
background-image: url('../img/background.jpg');

/* 修改后（如果需要） */
background-image: url('/9.23javaee/img/background.jpg');
```

### Q4: 字体文件加载失败

**解决方案**:
1. 检查字体文件是否已复制
2. 验证 `@font-face` 路径配置
3. 确认浏览器支持该字体格式

---

## 📝 路径配置说明

### 默认路径结构

```
webapp/
├── img/           # 主题图片资源
├── images/        # 项目自定义图片
├── css/           # 样式文件
├── js/            # JavaScript 文件
├── fonts/         # 字体文件
├── uploads/       # 用户上传文件
└── favicon.ico    # 网站图标
```

### CSS 中的路径引用

- **相对路径**: `../img/icon.png`
- **绝对路径**: `/9.23javaee/img/icon.png`（推荐用于生产环境）
- **上下文路径**: `${pageContext.request.contextPath}/img/icon.png`（JSP 中）

---

## 🎨 自定义主题资源

如果需要自定义主题样式：

1. **修改图片**: 直接替换 `img/` 目录下的文件
2. **修改样式**: 编辑 `css/butterfly.css`
3. **添加字体**: 将字体文件放入 `fonts/` 并在 CSS 中引用

### 示例：自定义 favicon

```html
<!-- 在 JSP 页面的 <head> 中添加 -->
<link rel="icon" href="${pageContext.request.contextPath}/favicon.ico" type="image/x-icon">
<link rel="shortcut icon" href="${pageContext.request.contextPath}/favicon.ico" type="image/x-icon">
```

---

## 📚 相关文档

- [Hexo Butterfly 主题文档](https://butterfly.js.org/)
- [JavaBlog 项目 README](../../README.md)
- [Maven 项目配置](../../pom.xml)

---

## 💡 提示

- **首次部署**: 必须复制所有主题资源
- **更新主题**: 仅需复制修改过的文件
- **版本管理**: 建议将主题资源添加到 Git 仓库
- **性能优化**: 考虑使用 CDN 托管静态资源

---

<div align="center">
  <p>如有问题，请查看项目主 README 或提交 Issue</p>
</div>

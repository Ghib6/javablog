# 静态资源使用说明

本目录用于存放博客系统使用的图片资源，包括：

- 文章缩略图
- 主题图标和装饰图片
- 用户上传的图片
- 网站 favicon 等

## 📁 目录结构

```
img/
├── favicon.ico          # 网站图标
├── butterfly-icon.png   # Butterfly 主题图标
├── 404.jpg             # 404 错误页面图片
├── error-page.png      # 错误页面背景
├── friend_404.gif      # 友链 404 提示
└── wechat.jpg          # 微信二维码等社交图片
```

## 🎨 主题资源集成

如果你使用了 Hexo Butterfly 主题或其他主题，需要将主题的静态资源复制到本目录。

### Windows PowerShell 脚本

```powershell
# 设定源目录与目标目录（根据实际路径调整）
$themeSrc = "你的主题路径\source\img"
$projectDst = "你的项目路径\src\main\webapp\img"

# 创建目标目录（如果不存在）
If (-Not (Test-Path $projectDst)) { 
    New-Item -ItemType Directory -Path $projectDst -Force 
}

# 复制所有图片文件
Get-ChildItem -Path $themeSrc -File | ForEach-Object { 
    Copy-Item $_.FullName -Destination $projectDst -Force 
}

# 复制 favicon 到 webapp 根目录
$faviconSrc = "$themeSrc\favicon.ico"
$faviconDst = "你的项目路径\src\main\webapp\favicon.ico"
If (Test-Path $faviconSrc) {
    Copy-Item $faviconSrc -Destination $faviconDst -Force
}

Write-Host "资源复制完成！" -ForegroundColor Green
```

### Linux/Mac 脚本

```bash
#!/bin/bash

# 设定源目录与目标目录（根据实际路径调整）
THEME_SRC="你的主题路径/source/img"
PROJECT_DST="你的项目路径/src/main/webapp/img"

# 创建目标目录
mkdir -p "$PROJECT_DST"

# 复制所有图片文件
cp -rf "$THEME_SRC"/* "$PROJECT_DST/"

# 复制 favicon
if [ -f "$THEME_SRC/favicon.ico" ]; then
    cp "$THEME_SRC/favicon.ico" "你的项目路径/src/main/webapp/"
fi

echo "资源复制完成！"
```

## 🔤 字体资源

如果主题使用了自定义字体，同样需要复制 `fonts` 目录：

```powershell
# PowerShell
$fontsSrc = "主题路径\source\fonts"
$fontsDst = "项目路径\src\main\webapp\fonts"
Copy-Item -Path $fontsSrc -Destination $fontsDst -Recurse -Force
```

```bash
# Linux/Mac
cp -rf "主题路径/source/fonts" "项目路径/src/main/webapp/fonts"
```

## ✅ 验证资源

复制完成后，建议进行以下检查：

1. **启动应用**：部署并启动 Web 应用
2. **打开浏览器开发者工具**：按 F12 打开
3. **检查 Network 选项卡**：查看是否有 404 错误
4. **检查 Console 选项卡**：查看是否有资源加载错误

### 常见问题修复

如果发现资源路径错误，可能需要调整 CSS 文件中的路径引用：

**错误示例**：
```css
background-image: url('/themes/butterfly/source/img/bg.jpg');
```

**正确路径**：
```css
background-image: url('/项目上下文路径/img/bg.jpg');
/* 例如：url('/9.23javaee/img/bg.jpg') */
```

## 📤 上传文件目录

用户上传的文章缩略图默认存储在 `webapp/uploads` 目录，确保该目录：

1. 存在且可写
2. 配置了合适的大小限制（当前为 20MB）
3. 定期清理无用文件

## 🔒 安全建议

- 限制允许上传的文件类型（只允许图片格式）
- 验证文件内容，防止恶意文件上传
- 对文件名进行清理，避免路径遍历攻击
- 设置文件大小限制
- 定期扫描上传目录

## 📝 注意事项

1. **版权问题**：确保使用的图片和主题资源符合许可证要求
2. **文件大小**：优化图片大小以提高加载速度
3. **备份**：定期备份重要的图片资源
4. **CDN**：生产环境建议使用 CDN 加速静态资源访问

---

更多信息请参考项目主 README.md 文档。

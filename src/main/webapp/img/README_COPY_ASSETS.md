# 主题资源配置说明

本目录用于存放博客前端主题所需的图片和静态资源文件。

## 资源文件说明

项目使用了 Hexo Butterfly 主题样式，需要以下资源文件：

### 图标和图片

- `favicon.ico`：网站图标
- `butterfly-icon.png`：主题标识图标
- `404.jpg`：404 错误页面背景图
- `error-page.png`：错误页面图标
- `friend_404.gif`：友链 404 图标
- `wechat.jpg`：微信二维码图片

### 其他静态资源

- 如果主题使用自定义字体，需要将 `fonts/` 目录复制到 `src/main/webapp/fonts`
- 如果使用其他图标或背景图，请放置在相应目录

## 如何添加主题资源

### 方式一：从 Hexo 主题复制（Windows PowerShell）

如果你有现有的 Hexo Butterfly 主题，可以使用以下命令复制资源：

```powershell
# 设定源目录与目标目录（根据实际路径调整）
$src = "你的Hexo主题路径\themes\hexo-theme-butterfly\source\img"
$dst = "你的项目路径\src\main\webapp\img"

# 创建目标目录（如果不存在）
If (-Not (Test-Path $dst)) { 
    New-Item -ItemType Directory -Path $dst -Force 
}

# 复制所有图片文件
Get-ChildItem -Path $src -File | ForEach-Object { 
    Copy-Item $_.FullName -Destination $dst -Force 
}

# 复制 favicon 到 webapp 根目录
Copy-Item "$src\favicon.ico" "你的项目路径\src\main\webapp\favicon.ico" -Force
```

### 方式二：从 Hexo 主题复制（Linux/Mac）

```bash
# 设定源目录与目标目录（根据实际路径调整）
SRC="你的Hexo主题路径/themes/hexo-theme-butterfly/source/img"
DST="你的项目路径/src/main/webapp/img"

# 创建目标目录
mkdir -p "$DST"

# 复制图片文件
cp -r "$SRC"/* "$DST/"

# 复制 favicon
cp "$SRC/favicon.ico" "你的项目路径/src/main/webapp/favicon.ico"
```

### 方式三：手动配置

1. 在 `src/main/webapp/img` 目录下放置所需的图片文件
2. 确保文件名与主题 CSS 中引用的路径一致
3. 将 `favicon.ico` 放置在 `src/main/webapp/` 根目录

## 验证资源配置

复制完成后，请执行以下步骤验证：

1. 启动应用程序
2. 打开浏览器访问博客首页
3. 按 F12 打开开发者工具，切换到 Console 或 Network 标签
4. 检查是否有 404 错误或资源加载失败的提示
5. 如发现资源路径错误，请检查：
   - 文件名是否正确
   - CSS 文件中的引用路径是否正确
   - 文件是否有读取权限

## 自定义主题资源

如果你想使用自己的图片资源：

1. 将图片文件放置在 `src/main/webapp/img/` 目录下
2. 修改对应的 CSS 文件（通常在 `src/main/webapp/css/` 目录）
3. 更新 JSP 页面中的图片引用路径
4. 重新构建和部署应用

## 注意事项

- 图片文件建议使用 WebP 格式以获得更好的性能
- 大图片建议压缩后再使用
- 确保图片文件名不包含中文或特殊字符
- favicon.ico 应该是 16x16 或 32x32 像素的图标文件

## 常见问题

### Q: 图片无法显示？

A: 检查：
- 图片文件是否存在于正确的目录
- 文件权限是否正确
- 浏览器控制台是否有 404 错误

### Q: 样式显示异常？

A: 确认：
- CSS 文件中的图片路径是否正确
- 图片格式是否被浏览器支持
- 是否清除了浏览器缓存

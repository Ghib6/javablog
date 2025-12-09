说明：将 Hexo 主题的图片与二进制资源复制到 Java webapp

请在本地终端 (PowerShell) 中执行下面命令，将主题目录下的图片和 favicon 复制到项目 `webapp`：

```powershell
# 设定源目录与目标目录（根据实际路径调整）
$src = "C:\BOKE\Blog\themes\hexo-theme-butterfly\source\img"
$dst = "D:\javadaima\blog1122\src\main\webapp\img"

If (-Not (Test-Path $dst)) { New-Item -ItemType Directory -Path $dst -Force }
Get-ChildItem -Path $src -File | ForEach-Object { Copy-Item $_.FullName -Destination $dst -Force }

# 复制 favicon 到 webapp 根（head 中通常引用 /favicon.ico）
Copy-Item "C:\BOKE\Blog\themes\hexo-theme-butterfly\source\img\favicon.ico" "D:\javadaima\blog1122\src\main\webapp\favicon.ico" -Force
```

提示：

- 上面命令会拷贝 `wechat.jpg`、`friend_404.gif`、`favicon.ico`、`error-page.png`、`butterfly-icon.png`、`404.jpg` 等文件到 `src/main/webapp/img`。
- 如果主题使用字体（例如 `fonts/`），请同样把主题的 `fonts` 目录复制到 `src/main/webapp/fonts`。
- 复制完成后，打开浏览器开发者工具查看是否有 404 或路径错误，若有我可以帮助修正 `butterfly.css` 中的相对路径。

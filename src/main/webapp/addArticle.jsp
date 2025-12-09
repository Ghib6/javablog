<%-- Created by IntelliJ IDEA. User: Ghib6 Date: 2025/9/30 Time: 15:08 To change this template use File | Settings |
  File Templates. --%>
  <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
      <c:set var="isEdit" value="${not empty param.action and param.action == 'edit'}" />
      <html>

      <head>
        <title>${isEdit ? '编辑文章' : '新建博客'}</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link rel="stylesheet" href="css/bootstrap.min.css">
        <link rel="stylesheet" href="css/easyMDE/easymde.min.css">
        <link rel="stylesheet" href="css/font-awesome.min.css">
        <link rel="stylesheet" href="css/addArticle.css">
        <link rel="stylesheet" href="css/hexo/butterfly.css">
        <link rel="stylesheet" href="css/hexo/custom.css">
        <link rel="stylesheet" href="css/hexo/progress_bar.css">
        <link rel="stylesheet" href="css/hexo/universe.css">
      </head>

      <body class="bg-light">
        <div class="container py-4">
          <div class="form-container">
            <div class="d-flex flex-column flex-md-row justify-content-between align-items-md-center gap-3">
              <div>
                <p class="text-muted mb-1 small">${isEdit ? '编辑现有文章并保存修改' : '填写内容后发布新的博客文章'}</p>
                <h2 class="form-title mb-0">${isEdit ? '编辑文章' : '添加文章'}</h2>
              </div>
              <a class="btn btn-outline-secondary" href="showArticlelist">返回文章列表</a>
            </div>
            <hr class="my-4" />
            <form action="article?action=${isEdit ? 'update' : 'add'}&id=${isEdit ? param.id : ''}" method="post"
              enctype="multipart/form-data" class="needs-validation" novalidate>
              <div class="mb-3">
                <label for="title" class="form-label">标题</label>
                <input type="text" class="form-control" id="title" name="title" placeholder="例如：我的第一篇技术博客" required
                  value="${article.title}">
              </div>
              <div class="mb-4">
                <label for="markdown" class="form-label">内容（Markdown）</label>
                <textarea class="form-control" id="markdown" name="markdown"
                  rows="10">${empty article.markdown ? article.content : article.markdown}</textarea>
                <div class="form-text">支持 Markdown 语法，可使用工具栏按钮插入标题、代码、图片等。</div>
              </div>
              <div class="row g-3">
                <div class="col-md-6">
                  <label for="categories" class="form-label">分类</label>
                  <input type="text" class="form-control" id="categories" name="categories" placeholder="多个分类用英文逗号分隔"
                    value="${article.categories}">
                </div>
                <div class="col-md-6">
                  <label for="tags" class="form-label">标签</label>
                  <input type="text" class="form-control" id="tags" name="tags" placeholder="示例：Java, Web"
                    value="${article.tags}">
                </div>
              </div>
              <div class="row g-3 mt-1">
                <div class="col-md-6">
                  <label for="thumbnailFile" class="form-label">封面图片</label>
                  <input type="file" class="form-control" id="thumbnailFile" name="thumbnailFile" accept="image/*">
                  <c:if test="${not empty article.thumbnail}">
                    <div class="alert alert-light border mt-2 mb-0">
                      <div class="d-flex align-items-center justify-content-between">
                        <span>当前封面：</span>
                        <a class="btn btn-link btn-sm" href="${article.thumbnail}" target="_blank">查看原图</a>
                      </div>
                      <img src="${article.thumbnail}" alt="当前封面" class="img-fluid rounded mt-2"
                        style="max-height: 180px; object-fit: cover;">
                    </div>
                  </c:if>
                </div>
                <div class="col-md-6 d-flex align-items-center">
                  <div class="form-check form-switch mt-4">
                    <input class="form-check-input" type="checkbox" id="allowComment" name="allowComment" value="1"
                      ${article.allowComment ? 'checked' : '' }>
                    <label class="form-check-label" for="allowComment">允许评论</label>
                  </div>
                </div>
              </div>

              <div class="row g-3 mt-4">
                <div class="col-12 col-md-4">
                  <button type="submit" class="btn btn-primary w-100 py-2">${isEdit ? '保存修改' : '立即发布'}</button>
                </div>
                <div class="col-12 col-md-4">
                  <button class="btn btn-outline-secondary w-100 py-2" type="button"
                    onclick="window.location.href='showArticlelist'">返回列表</button>
                </div>
                <div class="col-12 col-md-4">
                  <button class="btn btn-success w-100 py-2" type="submit" name="action" value="save">保存草稿</button>
                </div>
              </div>
            </form>
          </div>
        </div>
        <!-- Markdown easyMDE编辑器 -->
        <script src="https://cdn.jsdelivr.net/npm/easymde/dist/easymde.min.js"></script>
        <script>
          document.addEventListener('DOMContentLoaded', function () {
            var textarea = document.getElementById('markdown');
            if (textarea) {
              var easyMDE = new EasyMDE({
                toolbar: ["bold", "italic", "heading", "|", "quote", "unordered-list", "ordered-list", "|", "link", "image",
                  "|", "preview", "side-by-side", "fullscreen", "|", "guide"
                ],
                element: textarea,
                spellChecker: false,
                autofocus: false,
                minHeight: "320px",
                forceSync: true,
                autosave: {
                  enabled: false
                },
                status: ["lines", "words", "cursor"]
              });

              // 初始化后刷新一次，避免首次渲染高度异常
              var cm = easyMDE.codemirror;
              setTimeout(function () {
                try {
                  cm.refresh();
                  cm.focus();
                } catch (err) {
                  console.warn('刷新 EasyMDE 失败', err);
                }
              }, 80);

              // 在表单提交前把 EasyMDE 的内容同步回 textarea，并做简单非空验证
              (function () {
                var form = textarea.closest('form');
                if (!form) return;
                form.addEventListener('submit', function (e) {
                  // 同步内容
                  textarea.value = easyMDE.value();
                  // 简单校验：内容不能为空
                  if (!textarea.value || textarea.value.trim() === '') {
                    e.preventDefault();
                    alert('文章内容不能为空');
                    try { easyMDE.codemirror.focus(); } catch (err) { }
                    return false;
                  }
                  // 允许提交，表单会按正常流程提交（multipart 文件上传也可）
                });
              })();
            }
          });
        </script>
        <script src="js/cursor.js"></script>
        <script src="js/light.js"></script>
        <script src="js/universe.js"></script>
      </body>

      </html>
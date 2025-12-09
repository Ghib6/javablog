// package com.example.servlet;

// import com.example.bean.Article;
// import com.example.util.ArticleUtil;

// import jakarta.servlet.ServletContext;
// import jakarta.servlet.ServletException;
// import jakarta.servlet.annotation.MultipartConfig;
// import jakarta.servlet.annotation.WebServlet;
// import jakarta.servlet.http.*;
// import jakarta.servlet.http.Part;
// import java.io.IOException;
// import java.io.InputStream;
// import java.nio.file.Files;
// import java.nio.file.Path;
// import java.nio.file.Paths;
// import java.nio.file.StandardCopyOption;
// import java.util.Collections;
// import java.util.Date;
// import java.util.List;
// import java.util.UUID;

// //@WebServlet("/article")
// @MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 20 * 1024 * 1024, maxRequestSize = 60 * 1024 * 1024)
// public class ArticleServlet extends HttpServlet {
//     @Override
//     protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//         req.setCharacterEncoding("UTF-8");
//         resp.setContentType("text/html;charset=UTF-8");

//         String action = req.getParameter("action");
//         ArticleUtil util = new ArticleUtil();

//         if ("update".equals(action)) {
//             String idStr = req.getParameter("id");
//             if (idStr == null) {
//                 resp.getWriter().write("未指定ID");
//                 return;
//             }
//             try {
//                 int id = Integer.parseInt(idStr);
//                 Article article = util.getArticleById(id);
//                 if (article == null) {
//                     resp.getWriter().write("未找到要修改的文章");
//                     return;
//                 }
//                 article.setTitle(req.getParameter("title"));
//                 article.setMarkdown(resolveMarkdown(req));
//                 article.setCategories(req.getParameter("categories"));
//                 article.setTags(req.getParameter("tags"));
//                 article.setModified(new Date());
//                 article.setAllowComment(parseAllowComment(req));
//                 String uploadedPath = saveUploadedThumbnail(req);
//                 if (uploadedPath != null) {
//                     article.setThumbnail(uploadedPath);
//                 }
//                 util.updateArticle(article);
//                 // 简单处理：成功则重定向回列表
//                 resp.sendRedirect("showArticlelist");
//             } catch (NumberFormatException e) {
//                 resp.getWriter().write("ID格式错误");
//             }
//         } else if ("delete".equals(action)) {
//             String idStr = req.getParameter("id");
//             int res = 0;
//             if (idStr != null) {
//                 try {
//                     int id = Integer.parseInt(idStr);
//                     res = util.deleteArticle(id);
//                 } catch (NumberFormatException e) {
//                     resp.getWriter().write("ID格式错误");
//                     return;
//                 }
//             }
//             if (res > 0) {
//                 resp.sendRedirect("showArticlelist");
//             } else {
//                 resp.getWriter().write("未删除任何文章");
//             }
//         } else if ("search".equals(action)) {
//             String idStr = req.getParameter("id");
//             if (idStr != null) {
//                 try {
//                     int id = Integer.parseInt(idStr);
//                     Article article = util.getArticleById(id);
//                     if (article != null) {
//                         resp.getWriter().write("查询结果: " + article.toString());
//                     } else {
//                         resp.getWriter().write("未找到该ID的文章");
//                     }
//                 } catch (NumberFormatException e) {
//                     resp.getWriter().write("ID格式错误");
//                 }
//             }
//         } else if ("searchKeyword".equals(action)) {
//             String keyword = req.getParameter("keyword");
//             String category = req.getParameter("category");
//             List<Article> articles = util.searchArticles(keyword == null ? "" : keyword.trim(), category);
//             req.setAttribute("articleList", articles);
//             req.setAttribute("keyword", keyword);
//             req.setAttribute("category", category);
//             req.setAttribute("categories", util.getDistinctCategories());
//             req.setAttribute("categoryCounts", util.getCategoryCounts());
//             req.getRequestDispatcher("articleslist.jsp").forward(req, resp);
//         } else {
//             // 默认新增
//             String title = req.getParameter("title");
//             String markdown = resolveMarkdown(req);
//             String categories = req.getParameter("categories");
//             String tags = req.getParameter("tags");
//             boolean allowComment = parseAllowComment(req);
//             System.out.println(
//                     "Title: " + title + ",Tags: " + tags + ",Markdown length: "
//                             + (markdown == null ? 0 : markdown.length()) + ",AllowComment: " + allowComment);

//             Article article = new Article();
//             article.setTitle(title);
//             article.setMarkdown(markdown);
//             article.setCreated(new Date());
//             article.setCategories(categories);
//             article.setTags(tags);
//             article.setAllowComment(allowComment);
//             String uploadedPath = saveUploadedThumbnail(req);
//             if (uploadedPath != null) {
//                 article.setThumbnail(uploadedPath);
//             }

//             int res = util.addArticle(article);
//             if (res > 0) {
//                 // 新增成功则重定向回列表，与编辑操作保持一致的用户体验
//                 resp.sendRedirect("showArticlelist");
//             } else {
//                 resp.getWriter().write("插入文章失败");
//             }
//         }
//     }

//     @Override
//     protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//         req.setCharacterEncoding("UTF-8");
//         resp.setContentType("text/html;charset=UTF-8");

//         String action = req.getParameter("action");
//         ArticleUtil util = new ArticleUtil();

//         if ("view".equals(action)) {
//             String idStr = req.getParameter("id");
//             if (idStr != null) {
//                 try {
//                     int id = Integer.parseInt(idStr);
//                     Article article = util.getArticleById(id);
//                     if (article != null) {
//                         req.setAttribute("article", article);
//                         req.setAttribute("metaDescription", util.buildMetaDescription(article));
//                         req.setAttribute("comments", Boolean.TRUE.equals(article.getAllowComment())
//                                 ? util.getCommentsByArticleId(article.getId())
//                                 : Collections.emptyList());
//                         req.getRequestDispatcher("viewArticle.jsp").forward(req, resp);
//                         return;
//                     }
//                 } catch (NumberFormatException e) {
//                     resp.getWriter().write("ID格式错误");
//                     return;
//                 }
//             }
//             resp.getWriter().write("未指定ID");
//             return;
//         } else if ("edit".equals(action)) {
//             String idStr = req.getParameter("id");
//             if (idStr != null) {
//                 try {
//                     int id = Integer.parseInt(idStr);
//                     Article article = util.getArticleById(id);
//                     if (article != null) {
//                         req.setAttribute("article", article);
//                         req.getRequestDispatcher("addArticle.jsp").forward(req, resp);
//                         return;
//                     }
//                 } catch (NumberFormatException e) {
//                     resp.getWriter().write("ID格式错误");
//                     return;
//                 }
//             }
//             resp.getWriter().write("未指定ID");
//             return;
//         } else if ("update".equals(action)) {
//             String idStr = req.getParameter("id");
//             int res = 0;
//             if (idStr != null) {
//                 try {
//                     int id = Integer.parseInt(idStr);
//                     Article article = util.getArticleById(id);
//                     if (article != null) {
//                         String title = req.getParameter("title");
//                         String categories = req.getParameter("categories");
//                         String tags = req.getParameter("tags");
//                         article.setTitle(title);
//                         article.setMarkdown(resolveMarkdown(req));
//                         article.setCategories(categories);
//                         article.setTags(tags);
//                         article.setModified(new Date());
//                         article.setAllowComment(parseAllowComment(req));
//                         String uploadedPath = saveUploadedThumbnail(req);
//                         if (uploadedPath != null) {
//                             article.setThumbnail(uploadedPath);
//                         }
//                         res = util.updateArticle(article);
//                         resp.getWriter().write("修改结果: " + res);
//                     } else {
//                         resp.getWriter().write("未找到要修改的文章");
//                     }
//                 } catch (NumberFormatException e) {
//                     resp.getWriter().write("ID格式错误");
//                 }
//             }
//         } else if ("searchKeyword".equals(action)) {
//             String keyword = req.getParameter("keyword");
//             String category = req.getParameter("category");
//             List<Article> articles = util.searchArticles(keyword == null ? "" : keyword.trim(), category);
//             req.setAttribute("articleList", articles);
//             req.setAttribute("keyword", keyword);
//             req.setAttribute("category", category);
//             req.setAttribute("categories", util.getDistinctCategories());
//             req.setAttribute("categoryCounts", util.getCategoryCounts());
//             req.getRequestDispatcher("articleslist.jsp").forward(req, resp);
//         } else {
//             resp.getWriter().write("请指定正确的action参数");
//         }
//     }

//     private String saveUploadedThumbnail(HttpServletRequest req) throws IOException, ServletException {
//         if (!isMultipart(req)) {
//             return null;
//         }
//         Part part = req.getPart("thumbnailFile");
//         if (part == null || part.getSize() == 0) {
//             return null;
//         }
//         String submittedName = Paths.get(part.getSubmittedFileName()).getFileName().toString();
//         if (submittedName.trim().isEmpty()) {
//             return null;
//         }
//         String extension = "";
//         int dot = submittedName.lastIndexOf('.');
//         if (dot != -1) {
//             extension = submittedName.substring(dot);
//         }
//         String storedName = UUID.randomUUID() + extension;
//         ServletContext context = req.getServletContext();
//         String uploadDir = context.getRealPath("/uploads");
//         if (uploadDir == null) {
//             throw new ServletException("服务器无法解析 uploads 目录，请确保以 exploded 方式部署或手动配置上传目录");
//         }
//         System.out.println("[ArticleServlet] uploadDir = " + uploadDir);
//         Path uploadPath = Paths.get(uploadDir);
//         if (Files.notExists(uploadPath)) {
//             Files.createDirectories(uploadPath);
//         }
//         Path filePath = uploadPath.resolve(storedName);
//         try (InputStream input = part.getInputStream()) {
//             Files.copy(input, filePath, StandardCopyOption.REPLACE_EXISTING);
//         }

//         // --- 新增代码：同时保存到源码目录，防止IDE重启/热部署时丢失 ---
//         try {
//             // 尝试推断源码路径 (假设标准Maven结构且在target目录下运行)
//             // uploadDir 通常是 .../target/项目名/uploads
//             // 我们需要找到 .../src/main/webapp/uploads
//             String targetMarker = "target";
//             int targetIndex = uploadDir.lastIndexOf(targetMarker);
//             if (targetIndex > 0) {
//                 String projectRoot = uploadDir.substring(0, targetIndex);
//                 Path sourceUploadPath = Paths.get(projectRoot, "src", "main", "webapp", "uploads");

//                 // 如果源码目录存在，则复制一份过去
//                 if (Files.exists(sourceUploadPath)) {
//                     Path sourceFile = sourceUploadPath.resolve(storedName);
//                     Files.copy(filePath, sourceFile, StandardCopyOption.REPLACE_EXISTING);
//                     System.out.println("[ArticleServlet] 已同步保存图片到源码目录: " + sourceFile);
//                 }
//             }
//         } catch (Exception e) {
//             System.err.println("[ArticleServlet] 同步保存到源码目录失败 (非致命错误): " + e.getMessage());
//         }
//         // -----------------------------------------------------------

//         String url = req.getContextPath() + "/uploads/" + storedName;
//         System.out.println("[ArticleServlet] contextPath = " + req.getContextPath());
//         System.out.println("[ArticleServlet] thumbnail URL = " + url);
//         return url;
//     }

//     private boolean isMultipart(HttpServletRequest req) {
//         String contentType = req.getContentType();
//         return contentType != null && contentType.toLowerCase().startsWith("multipart/");
//     }

//     private boolean parseAllowComment(HttpServletRequest req) {
//         String value = req.getParameter("allowComment");
//         if (value == null) {
//             return false;
//         }
//         value = value.trim();
//         return "1".equals(value) || "true".equalsIgnoreCase(value) || "on".equalsIgnoreCase(value);
//     }

//     private String resolveMarkdown(HttpServletRequest req) {
//         String markdown = req.getParameter("markdown");
//         if (markdown == null || markdown.trim().isEmpty()) {
//             markdown = req.getParameter("content");
//         }
//         return markdown;
//     }
// }
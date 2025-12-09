package com.example.controller;

import com.example.bean.Article;
import com.example.util.ArticleUtil;
import com.example.util.Util;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Controller
public class ArticleController {

    private final ArticleUtil util = new ArticleUtil();

    @RequestMapping("/showArticlelist")
    public String showArticleList(HttpServletRequest request) throws ServletException, IOException {
        List<Article> articleList = util.getArticles();
        request.setAttribute("articleList", articleList);
        request.setAttribute("categories", util.getDistinctCategories());
        request.setAttribute("categoryCounts", util.getCategoryCounts());
        return "forward:articleslist.jsp";
    }

    @PostMapping(value = "/article", params = "action=update")
    public Object updateArticle(@RequestParam(value = "id", required = false) Integer id,
            HttpServletRequest req,
            @RequestParam(value = "thumbnailFile", required = false) MultipartFile thumbnailFile)
            throws ServletException, IOException {
        if (id == null) {
            return ResponseEntity.badRequest().body("未指定ID");
        }
        Article article = util.getArticleById(id);
        if (article == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("未找到要修改的文章");
        }
        article.setTitle(req.getParameter("title"));
        article.setMarkdown(Util.resolveMarkdown(req));
        article.setCategories(req.getParameter("categories"));
        article.setTags(req.getParameter("tags"));
        article.setModified(new Date());
        article.setAllowComment(Util.parseAllowComment(req));
        String uploadedPath = Util.saveUploadedThumbnail(req, thumbnailFile);
        if (uploadedPath != null) {
            article.setThumbnail(uploadedPath);
        }
        util.updateArticle(article);
        return "redirect:/showArticlelist";
    }

    @PostMapping(value = "/article", params = "action=delete")
    public Object deleteArticle(@RequestParam(value = "id", required = false) Integer id) {
        if (id == null) {
            return ResponseEntity.badRequest().body("未指定ID");
        }
        int res = util.deleteArticle(id);
        if (res > 0) {
            return "redirect:/showArticlelist";
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("未删除任何文章");
    }

    @PostMapping(value = "/article", params = "action=search")
    public ResponseEntity<String> searchById(@RequestParam(value = "id", required = false) Integer id) {
        if (id == null) {
            return ResponseEntity.badRequest().body("未指定ID");
        }
        Article article = util.getArticleById(id);
        if (article != null) {
            return ResponseEntity.ok("查询结果: " + article.toString());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("未找到该ID的文章");
    }

    @PostMapping(value = "/article", params = "action=searchKeyword")
    public String searchKeyword(@RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "category", required = false) String category,
            HttpServletRequest req) {
        List<Article> articles = util.searchArticles(keyword == null ? "" : keyword.trim(), category);
        req.setAttribute("articleList", articles);
        req.setAttribute("keyword", keyword);
        req.setAttribute("category", category);
        req.setAttribute("categories", util.getDistinctCategories());
        req.setAttribute("categoryCounts", util.getCategoryCounts());
        return "forward:articleslist.jsp";
    }

    @PostMapping("/article")
    public Object createArticle(HttpServletRequest req,
            @RequestParam(value = "thumbnailFile", required = false) MultipartFile thumbnailFile)
            throws ServletException, IOException {
        String title = req.getParameter("title");
        String markdown = Util.resolveMarkdown(req);
        String categories = req.getParameter("categories");
        String tags = req.getParameter("tags");
        boolean allowComment = Util.parseAllowComment(req);

        Article article = new Article();
        article.setTitle(title);
        article.setMarkdown(markdown);
        article.setCreated(new Date());
        article.setCategories(categories);
        article.setTags(tags);
        article.setAllowComment(allowComment);
        String uploadedPath = Util.saveUploadedThumbnail(req, thumbnailFile);
        if (uploadedPath != null) {
            article.setThumbnail(uploadedPath);
        }

        int res = util.addArticle(article);
        if (res > 0) {
            return "redirect:/showArticlelist";
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("插入文章失败");
    }

    @GetMapping(value = "/article", params = "action=view")
    public Object viewArticle(@RequestParam(value = "id", required = false) Integer id, HttpServletRequest req) {
        if (id == null) {
            return ResponseEntity.badRequest().body("未指定ID");
        }
        Article article = util.getArticleById(id);
        if (article != null) {
            req.setAttribute("article", article);
            req.setAttribute("metaDescription", util.buildMetaDescription(article));
            req.setAttribute("comments", Boolean.TRUE.equals(article.getAllowComment())
                    ? util.getCommentsByArticleId(article.getId())
                    : Collections.emptyList());
            return "forward:viewArticle.jsp";
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("未找到该ID的文章");
    }

    @GetMapping(value = "/article", params = "action=edit")
    public Object editArticle(@RequestParam(value = "id", required = false) Integer id, HttpServletRequest req) {
        if (id == null) {
            return ResponseEntity.badRequest().body("未指定ID");
        }
        Article article = util.getArticleById(id);
        if (article != null) {
            req.setAttribute("article", article);
            return "forward:addArticle.jsp";
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("未找到要修改的文章");
    }
}

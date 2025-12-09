package com.example.controller;

import com.example.bean.Article;
import com.example.util.ArticleUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class IndexController {

    private final ArticleUtil util = new ArticleUtil();

    @GetMapping("/index")
    public String index(@RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "category", required = false) String category,
            HttpServletRequest request) {
        List<Article> articleList = util.searchArticles(
                keyword == null ? "" : keyword.trim(),
                category);
        request.setAttribute("articleList", articleList);
        request.setAttribute("keyword", keyword);
        request.setAttribute("category", category);
        request.setAttribute("categories", util.getDistinctCategories());
        request.setAttribute("categoryCounts", util.getCategoryCounts());
        return "forward:index.jsp";
    }

    @PostMapping("/index")
    public String indexPost(@RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "category", required = false) String category,
            HttpServletRequest request) {
        return index(keyword, category, request);
    }
}

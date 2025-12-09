// package com.example.servlet;

// import com.example.bean.Article;
// import com.example.util.ArticleUtil;
// import jakarta.servlet.*;
// import jakarta.servlet.http.*;
// import jakarta.servlet.annotation.*;
// import java.io.IOException;
// import java.util.List;

// //@WebServlet(name = "ArticlelistServlet", value = "/showArticlelist")
// public class ArticleListServlet extends HttpServlet {
//     @Override
//     protected void doGet(HttpServletRequest request, HttpServletResponse response)
//             throws ServletException, IOException {
//         ArticleUtil util = new ArticleUtil();
//         List<Article> articleList = util.getArticles();
//         request.setAttribute("articleList", articleList);
//         request.setAttribute("categories", util.getDistinctCategories());
//         request.setAttribute("categoryCounts", util.getCategoryCounts());
//         // 转发到后台数据列表页
//         request.getRequestDispatcher("articleslist.jsp").forward(request, response);
//     }

//     @Override
//     protected void doPost(HttpServletRequest request, HttpServletResponse response)
//             throws ServletException, IOException {
//         // Handle POST requests if needed
//         doGet(request, response);
//     }
// }

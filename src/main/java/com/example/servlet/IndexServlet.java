// package com.example.servlet;

// import com.example.bean.Article;
// import com.example.util.ArticleUtil;
// import jakarta.servlet.ServletException;
// import jakarta.servlet.http.HttpServlet;
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;

// import java.io.IOException;
// import java.util.List;

// /**
//  * 前台博客首页入口，根据关键字和分类筛选文章并转发到 index.jsp。
//  */
// // @WebServlet(name = "IndexServlet", value = { "/index" })
// public class IndexServlet extends HttpServlet {

//     @Override
//     protected void doGet(HttpServletRequest request, HttpServletResponse response)
//             throws ServletException, IOException {
//         request.setCharacterEncoding("UTF-8");

//         String keyword = request.getParameter("keyword");
//         String category = request.getParameter("category");

//         ArticleUtil util = new ArticleUtil();
//         List<Article> articleList = util.searchArticles(
//                 keyword == null ? "" : keyword.trim(),
//                 category);

//         request.setAttribute("articleList", articleList);
//         request.setAttribute("keyword", keyword);
//         request.setAttribute("category", category);
//         request.setAttribute("categories", util.getDistinctCategories());
//         request.setAttribute("categoryCounts", util.getCategoryCounts());

//         request.getRequestDispatcher("index.jsp").forward(request, response);
//     }

//     @Override
//     protected void doPost(HttpServletRequest request, HttpServletResponse response)
//             throws ServletException, IOException {
//         doGet(request, response);
//     }
// }

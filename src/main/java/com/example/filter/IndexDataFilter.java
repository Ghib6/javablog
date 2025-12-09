package com.example.filter;

import com.example.bean.Article;
import com.example.util.ArticleUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import java.io.IOException;
import java.util.List;

/**
 * 保证直接访问 index.jsp 时也能拿到文章数据。
 * 当请求进入 /index.jsp，如果 request 中没有 articleList，则查询数据库并塞入。
 */
@WebFilter(filterName = "IndexDataFilter", urlPatterns = { "/index.jsp" })
public class IndexDataFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        // 若未通过控制器设置过列表，则在这里补充数据
        if (request.getAttribute("articleList") == null) {
            ArticleUtil util = new ArticleUtil();
            List<Article> list = util.getArticles();
            request.setAttribute("articleList", list);
        }
        chain.doFilter(request, response);
    }
}

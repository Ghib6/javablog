package com.example.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

// Java Web 身份验证过滤器(Authentication Filter)，主要用于对特定的 URL 请求进行登录状态检查。
// 说明：
// - 保持新增、编辑、删除等管理操作需要登录
// - 允许普通用户直接访问文章列表和查看文章详情（/article?action=view）
// - 对 /article?action=edit&id=... 等编辑类请求进行过滤
@WebFilter(filterName = "AuthFilter", urlPatterns = { "/addArticle.jsp", "/showArticlelist", "/articleslist.jsp",
    "/main.jsp", "/article" })
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        // 只对 /article?action=edit... 之类的管理操作进行登录校验
        String servletPath = req.getServletPath();
        String action = req.getParameter("action");

        boolean needCheckArticle = "/article".equals(servletPath)
                && action != null
                && !"view".equalsIgnoreCase(action); // 非 view 的 /article 请求才需要校验

        boolean loggedIn = (session != null && session.getAttribute("adminUser") != null);

        if (needCheckArticle || "/addArticle.jsp".equals(servletPath)
                || "/showArticlelist".equals(servletPath)
                || "/articleslist.jsp".equals(servletPath)
                || "/main.jsp".equals(servletPath)) {

            if (loggedIn) {
                chain.doFilter(request, response);
            } else {
                resp.sendRedirect(req.getContextPath() + "/login");
            }
        } else {
            // 其余请求（包括 /article?action=view）直接放行
            chain.doFilter(request, response);
        }
    }
}

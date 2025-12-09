<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
            <%@ page import="com.example.util.ArticleUtil, java.util.Map" %>
                <% if (request.getAttribute("categoryCounts")==null) { try { ArticleUtil _util=new ArticleUtil();
                    Map<String, Integer> _counts = _util.getCategoryCounts();
                    request.setAttribute("categoryCounts", _counts);
                    if (request.getAttribute("categories") == null) {
                    request.setAttribute("categories", _util.getDistinctCategories());
                    }
                    } catch (Exception _e) {
                    // ignore
                    }
                    }
                    %>
                    <!DOCTYPE html>
                    <html lang="zh-CN">

                    <head>
                        <meta charset="UTF-8" />
                        <meta name="viewport" content="width=device-width, initial-scale=1" />
                        <title>XBY's Blog</title>
                        <link rel="stylesheet" href="css/bootstrap.min.css" />
                        <link rel="stylesheet" href="css/font-awesome.min.css" />
                        <link rel="stylesheet" href="css/materialdesignicons.min.css" />

                        <!-- Butterfly CSS (normalize -> butterfly -> overrides) -->
                        <link rel="stylesheet" href="css/hexo/custom.css" />
                        <link rel="stylesheet" href="css/hexo/progress_bar.css" />
                        <link rel="stylesheet" href="css/hexo/universe.css" />
                        <link rel="stylesheet" href="css/_third-party/normalize.min.css" />
                        <link rel="stylesheet" href="css/hexo/butterfly.css" />

                    </head>

                    <body class="bg-light">
                        <!-- 顶部导航 -->
                        <nav class="navbar navbar-expand-lg navbar-light bg-white shadow-sm sticky-top">
                            <div class="container">
                                <a class="navbar-brand fw-bold" href="login.jsp">
                                    <span class="mdi mdi-home-outline"></span> XBY's Blog
                                </a>
                                <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
                                    data-bs-target="#navBar">
                                    <span class="navbar-toggler-icon"></span>
                                </button>
                                <div class="collapse navbar-collapse" id="navBar">
                                    <ul class="navbar-nav me-auto mb-2 mb-lg-0 gap-lg-3">
                                        <li class="nav-item"><a class="nav-link active" href="index.jsp"><i
                                                    class="fas fa-home"></i> 首页</a></li>
                                        <li class="nav-item"><a class="nav-link" href="#"><i class="fas fa-archive"></i>
                                                归档</a></li>
                                        <li class="nav-item dropdown">
                                            <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown"
                                                role="button" data-bs-toggle="dropdown" aria-expanded="false">
                                                <i class="fas fa-list"></i> 列表
                                            </a>
                                            <ul class="dropdown-menu" aria-labelledby="navbarDropdown">
                                                <li><a class="dropdown-item" href="#"><i class="fas fa-music"></i>
                                                        音乐</a></li>
                                                <li><a class="dropdown-item" href="#"><i class="fas fa-video"></i>
                                                        电影</a></li>
                                            </ul>
                                        </li>
                                        <li class="nav-item"><a class="nav-link" href="#"><i
                                                    class="fas fa-envelope-open"></i> 留言板</a></li>
                                        <li class="nav-item"><a class="nav-link" href="#"><i class="fas fa-heart"></i>
                                                关于</a></li>
                                    </ul>
                                    <form class="d-flex gap-2" action="index" method="get">
                                        <input class="form-control" type="search" name="keyword"
                                            placeholder="输入标题或标签关键字" value="${keyword}" />
                                        <button class="btn btn-outline-primary" type="submit">搜索</button>
                                    </form>
                                </div>
                            </div>
                        </nav>

                        <!-- 使用 Butterfly 近似布局 -->
                        <div id="body-wrap">
                            <main class="layout" id="content-inner">
                                <div class="recent-posts nc" id="recent-posts">
                                    <div class="recent-post-items">
                                        <c:choose>
                                            <c:when test="${not empty articleList}">
                                                <c:forEach items="${articleList}" var="item" varStatus="status">
                                                    <div class="recent-post-item wow animate__zoomIn"
                                                        data-wow-duration="1s" data-wow-delay="1s" data-wow-offset="100"
                                                        data-wow-iteration="1">
                                                        <c:choose>
                                                            <c:when test="${status.index % 2 == 0}">
                                                                <div class="post_cover left">
                                                                    <a href="article?action=view&id=${item.id}"
                                                                        title="${item.title}">
                                                                        <img class="post-bg"
                                                                            src="${empty item.thumbnail ? 'images/gallery/rocket.png' : item.thumbnail}"
                                                                            onerror="this.onerror=null;this.src='images/404.jpg'"
                                                                            alt="${item.title}">
                                                                    </a>
                                                                </div>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <div class="post_cover right">
                                                                    <a href="article?action=view&id=${item.id}"
                                                                        title="${item.title}">
                                                                        <img class="post-bg"
                                                                            src="${empty item.thumbnail ? 'images/gallery/rocket.png' : item.thumbnail}"
                                                                            onerror="this.onerror=null;this.src='images/404.jpg'"
                                                                            alt="${item.title}">
                                                                    </a>
                                                                </div>
                                                            </c:otherwise>
                                                        </c:choose>
                                                        <div class="recent-post-info">
                                                            <a class="article-title"
                                                                href="article?action=view&id=${item.id}"
                                                                title="${item.title}">${item.title}</a>
                                                            <div class="article-meta-wrap">
                                                                <span class="post-meta-date">
                                                                    <i class="far fa-calendar-alt"></i>
                                                                    <span class="article-meta-label">发表于</span>
                                                                    <time class="post-meta-date-created"
                                                                        title="发表于 ${item.created}">${item.created}</time>
                                                                    <span class="article-meta-separator">|</span>
                                                                    <i class="fas fa-history"></i>
                                                                    <span class="article-meta-label">更新于</span>
                                                                    <time class="post-meta-date-updated"
                                                                        title="更新于 ${item.created}">${item.created}</time>
                                                                </span>
                                                                <span class="article-meta">
                                                                    <span class="article-meta-separator">|</span>
                                                                    <i class="fas fa-inbox"></i>
                                                                    <a class="article-meta__categories"
                                                                        href="#">${item.categories}</a>
                                                                </span>
                                                                <span class="article-meta tags">
                                                                    <span class="article-meta-separator">|</span>
                                                                    <i class="fas fa-tag"></i>
                                                                    <a class="article-meta__tags"
                                                                        href="#">${item.tags}</a>
                                                                </span>
                                                            </div>
                                                            <c:set var="rawSnippet"
                                                                value="${empty item.markdown ? item.content : item.markdown}" />
                                                            <div class="content">
                                                                <c:choose>
                                                                    <c:when test="${fn:length(rawSnippet) > 120}">
                                                                        ${fn:substring(rawSnippet, 0, 120)}...
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        ${rawSnippet}
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </c:forEach>
                                            </c:when>
                                            <c:otherwise>
                                                <div class="alert alert-info">暂无文章，去 <a href="addArticle.jsp">发布一篇</a>
                                                    吧～</div>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>

                                <div class="aside-content" id="aside-content">
                                    <div class="card-widget card-info text-center wow animate__zoomIn"
                                        data-wow-delay="0.1s">
                                        <div class="avatar-img">
                                            <img src="https://picbed.ghib6.com/img/touxiang.jpg"
                                                onerror="this.onerror=null;this.src='images/404.jpg'" alt="avatar" />
                                        </div>
                                        <div class="author-info-name">XBY</div>
                                        <div class="author-info-description">欢迎来到我的博客</div>
                                        <div class="site-data">
                                            <a href="#">
                                                <div class="headline">文章</div>
                                                <div class="length-num">${fn:length(articleList)}</div>
                                            </a>
                                            <a href="#">
                                                <div class="headline">标签</div>
                                                <div class="length-num">0</div>
                                            </a>
                                            <a href="#">
                                                <div class="headline">分类</div>
                                                <div class="length-num">${fn:length(categoryCounts)}</div>
                                            </a>
                                        </div>
                                        <a id="card-info-btn" target="_blank" href="https://github.com/Ghib6"><i
                                                class="fab fa-github"></i><span>关注我</span></a>
                                        <div class="card-info-social-icons">
                                            <a class="social-icon" href="https://github.com/Ghib6" target="_blank"
                                                title="Github"><i class="fab fa-github" style="color: #24292e;"></i></a>
                                            <a class="social-icon" href="mailto:2060316191@qq.com" target="_blank"
                                                title="Email"><i class="fas fa-envelope"
                                                    style="color: #4a7dbe;"></i></a>
                                            <a class="social-icon" href="https://space.bilibili.com/324065498"
                                                target="_blank" title="bilibili"><i class="fab fa-bilibili"
                                                    style="color: #F45A8D;"></i></a>
                                        </div>
                                    </div>

                                    <div class="card-widget card-announcement wow animate__zoomIn"
                                        data-wow-delay="0.1s">
                                        <div class="item-headline"><i
                                                class="fas fa-bullhorn fa-shake"></i><span>公告</span>
                                        </div>
                                        <div class="announcement_content">欢迎来到我的博客</div>
                                    </div>

                                    <div class="sticky_layout">
                                        <div class="card-widget card-recent-post wow animate__zoomIn"
                                            data-wow-delay="0.1s">
                                            <div class="item-headline"><i class="fas fa-history"></i><span>最新文章</span>
                                            </div>
                                            <div class="aside-list">
                                                <c:choose>
                                                    <c:when test="${not empty articleList}">
                                                        <c:forEach items="${articleList}" var="item" end="4">
                                                            <div class="aside-list-item">
                                                                <a class="thumbnail"
                                                                    href="article?action=view&id=${item.id}"
                                                                    title="${item.title}">
                                                                    <img src="${empty item.thumbnail ? 'images/gallery/rocket.png' : item.thumbnail}"
                                                                        onerror="this.onerror=null;this.src='images/404.jpg'"
                                                                        alt="${item.title}">
                                                                </a>
                                                                <div class="content">
                                                                    <a class="title"
                                                                        href="article?action=view&id=${item.id}"
                                                                        title="${item.title}">${item.title}</a>
                                                                    <time datetime="${item.created}"
                                                                        title="发表于 ${item.created}">${item.created}</time>
                                                                </div>
                                                            </div>
                                                        </c:forEach>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <div class="aside-list-item">暂无文章</div>
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>
                                        </div>

                                        <div class="card-widget card-categories wow animate__zoomIn"
                                            data-wow-delay="0.1s">
                                            <div class="item-headline">
                                                <i class="fas fa-folder-open"></i>
                                                <span>分类</span>
                                            </div>
                                            <ul class="card-category-list" id="aside-cat-list">
                                                <c:choose>
                                                    <c:when test="${not empty categoryCounts}">
                                                        <c:forEach items="${categoryCounts}" var="entry">
                                                            <li class="card-category-list-item">
                                                                <a class="card-category-list-link"
                                                                    href="index?category=${entry.key}">
                                                                    <span
                                                                        class="card-category-list-name">${entry.key}</span>
                                                                    <span
                                                                        class="card-category-list-count">${entry.value}</span>
                                                                </a>
                                                            </li>
                                                        </c:forEach>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <li class="card-category-list-item">
                                                            <a class="card-category-list-link" href="#">
                                                                <span class="card-category-list-name">暂无分类</span>
                                                                <span class="card-category-list-count">0</span>
                                                            </a>
                                                        </li>
                                                    </c:otherwise>
                                                </c:choose>
                                            </ul>
                                        </div>
                                    </div>
                                </div>
                            </main>
                        </div>

                        <footer id="footer" style="background: transparent;">
                            <div id="footer-wrap">
                                <div class="copyright">&copy;2025 By XBY</div>
                                <div class="footer_custom_text"></div>
                            </div>
                        </footer>

                        <script src="js/jquery.min.js"></script>
                        <script src="js/bootstrap.min.js"></script>
                        <!-- Hexo/Butterfly theme scripts (placeholders + theme) -->
                        <script src="js/hexo/cursor.js"></script>
                        <script src="js/hexo/light.js"></script>
                        <script src="js/hexo/universe.js"></script>
                        <script src="js/hexo/utils.js"></script>
                        <script src="js/hexo/tw_cn.js"></script>
                        <script src="js/hexo/main.js"></script>
                        <script src="js/hexo/algolia.js"></script>
                        <script src="js/hexo/local-search.js"></script>
                    </body>

                    </html>
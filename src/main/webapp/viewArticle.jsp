<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
        <html>

        <head>
            <title>${article.title}</title>
            <meta name="description" content="${metaDescription}" />
            <meta property="og:title" content="${article.title}" />
            <meta property="og:description" content="${metaDescription}" />
            <link rel="stylesheet" href="css/bootstrap.min.css">
            <link rel="stylesheet"
                href="https://cdn.jsdelivr.net/npm/github-markdown-css@5.2.0/github-markdown-light.min.css" />
            <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/highlight.js@11.9.0/styles/github.min.css" />
            <link rel="stylesheet" href="css/_third-party/normalize.min.css" />
            <link rel="stylesheet" href="css/butterfly.css">
            <link rel="stylesheet" href="css/custom.css">
            <link rel="stylesheet" href="css/progress_bar.css">
            <link rel="stylesheet" href="css/universe.css">
            <style>
                .markdown-body {
                    padding: 1rem;
                    background-color: #fff;
                    border-radius: 0.5rem;
                }
            </style>
        </head>

        <body>
            <div id="body-wrap">
                <div class="layout">
                    <div>
                        <article class="post page">
                            <h1 class="article-title">${article.title}</h1>
                            <div class="article-meta text-muted mb-3">
                                <span class="me-3">📅 ${article.created}</span>
                                <span class="me-3">标签: ${article.tags}</span>
                                <span class="me-3">分类: ${article.categories}</span>
                            </div>
                            <div class="markdown-body">
                                <c:out value="${article.content}" escapeXml="false" />
                            </div>

                            <c:if test="${article.allowComment}">
                                <hr />
                                <h5>评论</h5>
                                <c:choose>
                                    <c:when test="${not empty comments}">
                                        <ul class="list-group">
                                            <c:forEach items="${comments}" var="comment">
                                                <li class="list-group-item">
                                                    <div class="d-flex justify-content-between align-items-center">
                                                        <strong>${comment.author}</strong>
                                                        <small class="text-muted">评论时间: ${comment.created}</small>
                                                    </div>
                                                    <div class="mt-2">${comment.content}</div>
                                                    <span class="badge bg-secondary mt-2">${comment.status}</span>
                                                </li>
                                            </c:forEach>
                                        </ul>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="alert alert-info mt-3">暂无评论</div>
                                    </c:otherwise>
                                </c:choose>
                            </c:if>

                        </article>
                    </div>

                    <aside id="aside-content">
                        <div class="card cardHover mb-3">
                            <div class="card-body">侧边栏内容</div>
                        </div>
                    </aside>
                </div>
            </div>
            <script src="https://cdn.jsdelivr.net/npm/highlight.js@11.9.0/lib/common.min.js"></script>
            <script>
                if (window.hljs) {
                    hljs.highlightAll();
                }
            </script>
            <script src="js/cursor.js"></script>
            <script src="js/light.js"></script>
            <script src="js/universe.js"></script>
            <script src="js/utils.js"></script>
            <script src="js/tw_cn.js"></script>
            <script src="js/main.js"></script>
            <script src="js/search/algolia.js"></script>
            <script src="js/search/local-search.js"></script>
        </body>

        </html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
  <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
    <%@ page isELIgnored="false" %>
      <%-- Created by IntelliJ IDEA. User: Ghib6 Date: 2025/10/14 Time: 15:14 To change this template use File |
        Settings | File Templates. --%>
        <%@ page contentType="text/html;charset=UTF-8" language="java" %>
          <html>

          <head>
            <title>博客列表</title>
            <link rel="stylesheet" type="text/css" href="css/materialdesignicons.min.css">
            <link rel="stylesheet" type="text/css" href="css/bootstrap.min.css">
            <link rel="stylesheet" type="text/css" href="css/animate.min.css">
            <link rel="stylesheet" type="text/css" href="js/bootstrap-multitabs/multitabs.min.css">
            <link rel="stylesheet" href="css/_third-party/normalize.min.css">
            <link rel="stylesheet" href="css/hexo/butterfly.css">
            <link rel="stylesheet" href="css/hexo/custom.css">
            <link rel="stylesheet" href="css/hexo/progress_bar.css">
            <link rel="stylesheet" href="css/hexo/universe.css">
          </head>

          <body>
            <div id="body-wrap">
              <div class="layout hide-aside" style="max-width: 1200px">
                <div>
                  <div class="card">
                    <div class="card-header d-flex justify-content-between align-items-center">
                      <h3 class="mb-0">最新文章</h3>
                      <a href="index.jsp" class="btn btn-outline-primary">返回博客首页</a>
                    </div>
                    <div class="card-body">
                      <form class="row g-2 mb-3" action="article" method="get">
                        <input type="hidden" name="action" value="searchKeyword" />
                        <div class="col-sm-8 col-md-6 col-lg-4">
                          <input type="text" class="form-control" name="keyword" placeholder="输入标题或标签关键字"
                            value="${keyword != null ? keyword : param.keyword}" />
                        </div>
                        <div class="col-sm-4 col-md-3 col-lg-2">
                          <select class="form-select" name="category">
                            <option value="">全部分类</option>
                            <c:forEach items="${categories}" var="cat">
                              <option value="${cat}" <c:if test="${cat == category}">selected</c:if>>${cat}</option>
                            </c:forEach>
                          </select>
                        </div>
                        <div class="col-sm-4 col-md-3 col-lg-2">
                          <button type="submit" class="btn btn-primary w-100">搜索</button>
                        </div>
                        <div class="col-sm-4 col-md-3 col-lg-2">
                          <a class="btn btn-outline-secondary w-100" href="showArticlelist">重置</a>
                        </div>
                        <div class="col-sm-4 col-md-3 col-lg-2">
                          <a class="btn btn-success w-100" href="addArticle.jsp">新增文章</a>
                        </div>
                      </form>
                      <table class="table">
                        <thead>
                          <tr>
                            <th>序号</th>
                            <th>标题</th>
                            <th>标签</th>
                            <th>时间</th>
                            <th>操作</th>
                          </tr>
                        </thead>
                        <tbody>
                          <c:choose>
                            <c:when test="${not empty articleList}">
                              <c:forEach items="${articleList}" var="item" varStatus="status">
                                <tr>
                                  <td>${status.count}</td>
                                  <td>
                                    <a href="article?action=view&id=${item.id}">${item.title}</a>
                                  </td>
                                  <td>${item.tags}</td>
                                  <td>${item.created}</td>
                                  <td>

                                    <a class="btn btn-sm btn-primary" href="article?action=edit&id=${item.id}">编辑</a>
                                    <form style="display:inline-block;margin-left:6px;" method="post" action="article"
                                      onsubmit="return confirm('确定要删除这篇文章吗？');">
                                      <input type="hidden" name="action" value="delete" />
                                      <input type="hidden" name="id" value="${item.id}" />
                                      <button type="submit" class="btn btn-sm btn-danger">删除</button>
                                    </form>
                                  </td>
                                </tr>
                              </c:forEach>
                            </c:when>
                            <c:otherwise>
                              <h3>暂无数据<h3>
                            </c:otherwise>
                          </c:choose>
                        </tbody>
                      </table>
                    </div>
                  </div>
                </div>
              </div>
            </div>
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
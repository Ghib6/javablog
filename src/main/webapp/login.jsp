<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <html>

        <head>
            <title>管理员登录</title>
            <link rel="stylesheet" href="css/bootstrap.min.css" />
            <link rel="stylesheet" href="css/butterfly.css" />
            <link rel="stylesheet" href="css/custom.css" />
            <link rel="stylesheet" href="css/progress_bar.css" />
            <link rel="stylesheet" href="css/universe.css" />
        </head>

        <body class="bg-light">
            <canvas id="universe"></canvas>
            <div class="container mt-5" style="max-width: 420px;">
                <div class="login-card p-4">
                    <h3 class="mb-4 text-center login-title">后台登录</h3>
                    <form method="post" action="login">
                        <div class="mb-3">
                            <label class="form-label">用户名</label>
                            <input type="text" class="form-control login-input" name="username" required />
                        </div>
                        <div class="mb-3">
                            <label class="form-label">密码</label>
                            <input type="password" class="form-control login-input" name="password" required />
                        </div>
                        <c:if test="${not empty error}">
                            <div class="alert alert-danger">${error}</div>
                        </c:if>
                        <button type="submit" class="btn btn-primary w-100 login-btn-primary">登录</button>
                    </form>
                </div>
            </div>
            <script src="js/cursor.js"></script>
            <script src="js/light.js"></script>
            <script src="js/universe.js"></script>
        </body>

        </html>
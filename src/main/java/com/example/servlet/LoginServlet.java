// package com.example.servlet;

// import com.example.bean.AdminUser;
// import com.example.util.AdminUserUtil;
// import jakarta.servlet.ServletException;
// import jakarta.servlet.http.HttpServlet;
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;
// import jakarta.servlet.http.HttpSession;

// import java.io.IOException;

// // @WebServlet("/login")
// public class LoginServlet extends HttpServlet {

//     private final AdminUserUtil adminUserUtil = new AdminUserUtil();

//     @Override
//     protected void doGet(HttpServletRequest req, HttpServletResponse resp)
//             throws ServletException, IOException {
//         req.getRequestDispatcher("login.jsp").forward(req, resp);
//     }

//     @Override
//     protected void doPost(HttpServletRequest req, HttpServletResponse resp)
//             throws ServletException, IOException {
//         req.setCharacterEncoding("UTF-8");
//         String username = req.getParameter("username");
//         String password = req.getParameter("password");

//         AdminUser user = adminUserUtil.findByUsername(username);
//         if (adminUserUtil.checkPassword(user, password)) {
//             HttpSession session = req.getSession(true);
//             session.setAttribute("adminUser", user);
//             resp.sendRedirect("showArticlelist");
//         } else {
//             req.setAttribute("error", "用户名或密码错误");
//             req.getRequestDispatcher("login.jsp").forward(req, resp);
//         }
//     }
// }

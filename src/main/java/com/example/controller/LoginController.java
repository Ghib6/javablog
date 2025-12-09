package com.example.controller;

import com.example.bean.AdminUser;
import com.example.util.AdminUserUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    private final AdminUserUtil adminUserUtil = new AdminUserUtil();

    @GetMapping("/login")
    public String loginPage() {
        return "forward:login.jsp";
    }

    @PostMapping("/login")
    public String login(@RequestParam("username") String username,
            @RequestParam("password") String password,
            HttpServletRequest req,
            HttpSession session) {
        AdminUser user = adminUserUtil.findByUsername(username);
        if (adminUserUtil.checkPassword(user, password)) {
            session.setAttribute("adminUser", user);
            return "redirect:/showArticlelist";
        }
        req.setAttribute("error", "用户名或密码错误");
        return "forward:login.jsp";
    }
}

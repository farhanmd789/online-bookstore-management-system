package com.bittercode.controller;

import com.bittercode.model.StoreException;
import com.bittercode.model.User;
import com.bittercode.model.UserRole;
import com.bittercode.service.UserService;
import com.bittercode.service.impl.UserServiceImpl;
import com.bittercode.util.StoreUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class AdminAuthController {

    private final UserService userService = new UserServiceImpl();

    @GetMapping("/adminlog")
    public String showAdminLoginOrDashboard(HttpSession session, Model model) {
        if (StoreUtil.isLoggedIn(UserRole.SELLER, session)) {
            String firstName = (String) session.getAttribute("seller_firstname");
            model.addAttribute("firstName", firstName != null ? firstName : "Admin");
            return "seller-dashboard";
        }
        return "SellerLogin";
    }

    @PostMapping("/adminlog")
    public String processAdminLogin(@RequestParam("username") String username,
                                    @RequestParam("password") String password,
                                    HttpSession session, Model model) {
        User user;
        try {
            user = userService.login(UserRole.SELLER, username, password, session);
        } catch (StoreException e) {
            model.addAttribute("errorMsg", "Login failed: " + e.getMessage());
            return "SellerLogin";
        }
        if (user != null) {
            session.setAttribute("seller_firstname", user.getFirstName());
            model.addAttribute("firstName", user.getFirstName());
            return "seller-dashboard";
        }
        model.addAttribute("errorMsg", "Incorrect Username or Password");
        return "SellerLogin";
    }
}

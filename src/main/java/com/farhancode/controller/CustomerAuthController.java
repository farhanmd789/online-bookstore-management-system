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
public class CustomerAuthController {

    private final UserService userService = new UserServiceImpl();

    @GetMapping("/userlog")
    public String showLoginOrDashboard(HttpSession session, Model model) {
        if (StoreUtil.isLoggedIn(UserRole.CUSTOMER, session)) {
            String firstName = (String) session.getAttribute("customer_firstname");
            model.addAttribute("firstName", firstName != null ? firstName : "Customer");
            return "customer-dashboard";
        }
        return "CustomerLogin";
    }

    @PostMapping("/userlog")
    public String processLogin(@RequestParam(value = "mailid", required = false) String mailId,
                               @RequestParam(value = "username", required = false) String username,
                               @RequestParam("password") String password,
                               HttpSession session, Model model) {
        if (mailId == null || mailId.isEmpty()) mailId = username;
        User user;
        try {
            user = userService.login(UserRole.CUSTOMER, mailId, password, session);
        } catch (StoreException e) {
            model.addAttribute("errorMsg", "Login failed: " + e.getMessage());
            return "CustomerLogin";
        }
        if (user != null) {
            session.setAttribute("customer_firstname", user.getFirstName());
            model.addAttribute("firstName", user.getFirstName());
            return "customer-dashboard";
        }
        model.addAttribute("errorMsg", "Incorrect Email or Password");
        return "CustomerLogin";
    }

    @GetMapping("/userreg")
    public String showRegister() {
        return "CustomerRegister";
    }

    @PostMapping("/userreg")
    public String processRegister(@RequestParam("mailid") String mailId,
                                  @RequestParam("password") String password,
                                  @RequestParam("firstname") String firstName,
                                  @RequestParam("lastname") String lastName,
                                  @RequestParam(value = "address", required = false) String address,
                                  @RequestParam(value = "phone", required = false) String phoneStr,
                                  Model model) {
        User user = new User();
        user.setEmailId(mailId);
        user.setPassword(password);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setAddress(address);
        if (phoneStr != null && !phoneStr.trim().isEmpty()) {
            try {
                user.setPhone(Long.parseLong(phoneStr.trim()));
            } catch (NumberFormatException e) {
                // ignore invalid phone
            }
        }
        String result;
        try {
            result = userService.register(UserRole.CUSTOMER, user);
        } catch (StoreException e) {
            model.addAttribute("errorMsg", "Registration failed: " + e.getMessage());
            return "CustomerRegister";
        }
        if ("SUCCESS".equals(result)) {
            model.addAttribute("successMsg", "Registration successful! Please login.");
            return "CustomerLogin";
        }
        model.addAttribute("errorMsg", "Registration failed: " + result);
        return "CustomerRegister";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        userService.logout(session);
        return "redirect:/login";
    }
}

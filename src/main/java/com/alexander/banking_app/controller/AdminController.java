package com.alexander.banking_app.controller;

import com.alexander.banking_app.entity.User;
import com.alexander.banking_app.repository.AccountRepository;
import com.alexander.banking_app.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    // admin dashboard
    @GetMapping("/admin")
    public String adminDashboard(HttpSession session, Model model) {

        // get session user
        User user = (User) session.getAttribute("loggedInUser");

        // restrict to admin
        if (user == null || !"ADMIN".equalsIgnoreCase(user.getRole())) {
            return "redirect:/login";
        }

        // send all users
        model.addAttribute("users", userRepository.findAll());

        // send all accounts
        model.addAttribute("accounts", accountRepository.findAll());

        return "admin-dashboard";
    }

    // view all users
    @GetMapping("/admin/users")
    public String viewAllUsers(HttpSession session, Model model) {

        // check admin
        User user = (User) session.getAttribute("loggedInUser");

        if (user == null || !"ADMIN".equalsIgnoreCase(user.getRole())) {
            return "redirect:/login";
        }

        // fetch users
        model.addAttribute("users", userRepository.findAll());

        return "all-users";
    }
}
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
    private UserRepository userRepository; // fetch users

    @Autowired
    private AccountRepository accountRepository; // fetch accounts

    @GetMapping("/admin")
    public String adminDashboard(HttpSession session, Model model) {

        User user = (User) session.getAttribute("loggedInUser"); // get session user

        // only admin allowed
        if (user == null || !"ADMIN".equals(user.getRole())) {
            return "redirect:/login";
        }

        model.addAttribute("users", userRepository.findAll()); // all users
        model.addAttribute("accounts", accountRepository.findAll()); // all accounts

        return "admin-dashboard"; // admin page
    }
}
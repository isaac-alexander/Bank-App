package com.alexander.banking_app.controller;

import com.alexander.banking_app.entity.User;
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

    // view all users page
    @GetMapping("/admin/users")
    public String viewAllUsers(HttpSession session, Model model) {

        // get session user
        User user = (User) session.getAttribute("loggedInUser");

        // admin only
        if (user == null || !"ADMIN".equalsIgnoreCase(user.getRole())) {
            return "redirect:/login";
        }

        // send users to view
        model.addAttribute("users", userRepository.findAll());

        return "all-users";
    }
}
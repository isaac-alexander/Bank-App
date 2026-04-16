package com.alexander.banking_app.controller;

import com.alexander.banking_app.entity.User;
import com.alexander.banking_app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@Controller
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    // view all users
    @GetMapping("/admin/users")
    public String viewAllUsers(Authentication authentication, Model model) {

        // check if user is logged in
        if (authentication == null) {
            return "redirect:/login";
        }

        // get username from spring security
        String username = authentication.getName();

        // fetch user as optional
        Optional<User> optionalUser = userRepository.findByUsername(username);

        // check if user exists
        if (optionalUser.isEmpty()) {
            return "redirect:/login";
        }

        // get actual user
        User user = optionalUser.get();

        // check if user is admin
        if (!"ADMIN".equalsIgnoreCase(user.getRole())) {
            return "redirect:/login";
        }

        // send all users to view
        model.addAttribute("users", userRepository.findAll());

        return "all-users";
    }
}
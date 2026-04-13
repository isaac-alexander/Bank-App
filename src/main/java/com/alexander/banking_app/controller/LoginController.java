package com.alexander.banking_app.controller;

import com.alexander.banking_app.entity.User;
import com.alexander.banking_app.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {

    @Autowired
    private UserService userService; // used to fetch user

    @GetMapping("/login")
    public String loginPage() {

        // show login page
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam String username,
                            @RequestParam String password,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {

        // find user by username
        User user = userService.findByUsername(username);

        // check if user exists and password matches
        if (user != null && user.getPassword().equals(password)) {

            // store logged in user in session
            session.setAttribute("loggedInUser", user);

            // admin goes to admin dashboard
            if ("ADMIN".equalsIgnoreCase(user.getRole())) {
                return "redirect:/admin";
            }

            // customer goes to user dashboard
            if ("CUSTOMER".equalsIgnoreCase(user.getRole())) {
                return "redirect:/dashboard";
            }

            // unknown role
            return "redirect:/login";
        }

        // error message
        redirectAttributes.addFlashAttribute("error", "incorrect username or password");

        return "redirect:/login";
    }
}
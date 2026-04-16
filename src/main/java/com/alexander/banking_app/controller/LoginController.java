package com.alexander.banking_app.controller;

import com.alexander.banking_app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String defaultRoute() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage(Authentication authentication) {


        if (authentication != null) {
            return "redirect:/dashboard"; // send to dashboard directly
        }

        return "login"; // show login page
    }

}
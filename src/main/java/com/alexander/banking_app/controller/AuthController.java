package com.alexander.banking_app.controller;

import com.alexander.banking_app.entity.User;
import com.alexander.banking_app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
@Controller
@RequestMapping("/")
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String defaultRoute() {

        return "redirect:/login";
    }

    @GetMapping("register")
    public String registerPage(Model model) {

        model.addAttribute("user", new User()); // send empty user object

        return "register";
    }

    @PostMapping("register")
    public String registerUser(@ModelAttribute User user) {

        userService.registerUser(user); // save user

        return "redirect:/login"; // go to login page after register
    }
}


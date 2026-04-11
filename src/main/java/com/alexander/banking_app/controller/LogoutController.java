package com.alexander.banking_app.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LogoutController {

    @GetMapping("/logout")
    public String logout(HttpSession session) {

        session.invalidate(); // clear session completely

        return "redirect:/login"; // go back to login page
    }
}
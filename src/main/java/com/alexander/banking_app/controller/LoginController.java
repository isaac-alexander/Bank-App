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
    private UserService userService;

    @GetMapping("/")
    public String defaultRoute() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage(HttpSession session) {

        // check if user already logged in
        User user = (User) session.getAttribute("loggedInUser");

        if (user != null) {
            return "redirect:/dashboard"; // send to dashboard directly
        }

        return "login"; // show login page
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam String username,
                            @RequestParam String password,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {

        // fetch user from database
        User user = userService.findByUsername(username);

        // validate credentials
        if (user != null && user.getPassword().equals(password)) {

            // store user in session
            session.setAttribute("loggedInUser", user);

            // all users go to dashboard
            return "redirect:/dashboard";
        }

        // invalid login
        redirectAttributes.addFlashAttribute("error", "incorrect username or password");

        return "redirect:/login";
    }
}
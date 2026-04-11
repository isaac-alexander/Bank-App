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

        return "login"; // show login page
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam String username,
                            @RequestParam String password,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {

        // find user by username
        User user = userService.findByUsername(username);

        //Java optional

        // check if valid
        if (user != null && user.getPassword().equals(password)) {

            session.setAttribute("loggedInUser", user); // store in session

            return "redirect:/dashboard"; // success
        }

        // send error
        redirectAttributes.addFlashAttribute("error", "incorrect username or password");

        return "redirect:/login"; // go back to login
    }
}
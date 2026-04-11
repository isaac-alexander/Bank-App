package com.alexander.banking_app.controller;

import com.alexander.banking_app.entity.Account;
import com.alexander.banking_app.entity.User;
import com.alexander.banking_app.repository.UserRepository;
import com.alexander.banking_app.service.AccountService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private UserRepository userRepository;


    // show all accounts for a user
    @GetMapping
    public String getUserAccounts(HttpSession session, Model model) {

        User user = (User) session.getAttribute("loggedInUser");

        if (user == null) {
            return "redirect:/login";
        }

        List<Account> accounts = accountService.getUserAccounts(user.getId());

        model.addAttribute("accounts", accounts);
        model.addAttribute("userId", user.getId());

        return "accounts";
    }

    // show create account form
    @GetMapping("/create/{userId}")
    public String showCreateForm(@PathVariable Long userId, Model model) {

        model.addAttribute("userId", userId); // send user id

        return "create-account"; // create-account.html
    }

    // handle create account
    @PostMapping("/create")
    public String createAccount(@RequestParam Long userId,
                                @RequestParam String accountType) {

        accountService.createAccount(userId, accountType); // create account

        return "redirect:/accounts";
    }

}

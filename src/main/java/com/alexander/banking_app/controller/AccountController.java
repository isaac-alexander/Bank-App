package com.alexander.banking_app.controller;

import com.alexander.banking_app.entity.Account;
import com.alexander.banking_app.entity.User;
import com.alexander.banking_app.service.AccountService;
import com.alexander.banking_app.repository.AccountRepository;
import com.alexander.banking_app.repository.UserRepository;
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
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    // show accounts for logged in user
    @GetMapping
    public String getAccounts(HttpSession session, Model model) {

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
    public String showCreate(@PathVariable Long userId, Model model) {

        model.addAttribute("userId", userId);

        return "create-account";
    }

    // handle account creation
    @PostMapping("/create")
    public String createAccount(@RequestParam Long userId,
                                @RequestParam String accountType,
                                Model model) {

        accountService.createAccount(userId, accountType);

        model.addAttribute("success", "account created successfully");

        return "redirect:/accounts";
    }

}

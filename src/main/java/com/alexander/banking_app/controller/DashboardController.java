package com.alexander.banking_app.controller;

import com.alexander.banking_app.entity.Account;
import com.alexander.banking_app.entity.Transaction;
import com.alexander.banking_app.entity.User;
import com.alexander.banking_app.repository.AccountRepository;
import com.alexander.banking_app.repository.UserRepository;
import com.alexander.banking_app.service.TransactionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class DashboardController {

    @Autowired
    private UserRepository userRepository; // access database

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {

        // get logged in user from session
        User user = (User) session.getAttribute("loggedInUser");

        // if user is not logged in redirect to login
        if (user == null) {
            return "redirect:/login";
        }

        // send user to html
        model.addAttribute("user", user);

        // get all accounts that belong to this user
        List<Account> accounts = accountRepository.findByUserId(user.getId());

        // create account variable and set it to null first
        // this prevents thymeleaf crash if no account exists
        Account account = null;

        // check if user has at least one account
        if (!accounts.isEmpty()) {

            // take the first account
            account = accounts.getFirst();
        }

        // send account (can be null)
        model.addAttribute("account", account);

        // if account exists get transactions
        if (account != null) {

            List<Transaction> transactions =
                    transactionService.getHistory(account.getId());

            model.addAttribute("transactions", transactions);
        }

        return "dashboard";
    }

}

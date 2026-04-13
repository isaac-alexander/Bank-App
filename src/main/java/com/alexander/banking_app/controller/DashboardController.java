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
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {

        // get logged in user from session
        User user = (User) session.getAttribute("loggedInUser");

        // redirect if not logged in
        if (user == null) {
            return "redirect:/login";
        }

        // send user to frontend
        model.addAttribute("user", user);

        // get user from database
        User dbUser = userRepository.findById(user.getId()).orElse(null);

        if (dbUser == null) {
            return "redirect:/login";
        }

        // get accounts
        List<Account> accounts = accountRepository.findByUser(dbUser);

        // default account
        Account account = null;

        // pick first account if exists
        if (!accounts.isEmpty()) {
            account = accounts.getFirst();
        }

        // send account to frontend
        model.addAttribute("account", account);

        // load transactions if account exists
        if (account != null) {

            List<Transaction> transactions =
                    transactionService.getHistory(account.getId());

            model.addAttribute("transactions", transactions);
        }

        return "dashboard";
    }
}
package com.alexander.banking_app.controller;

import com.alexander.banking_app.entity.Transaction;
import com.alexander.banking_app.entity.User;
import com.alexander.banking_app.service.TransactionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    // get transaction history
    @GetMapping("/{accountId}")
    public String getHistory(@PathVariable Long accountId, Model model) {

        List<Transaction> list = transactionService.getHistory(accountId); // fetch transactions

        model.addAttribute("transactions", list); // send to html
        model.addAttribute("accountId", accountId); // keep account id

        return "transactions";
    }

    // show admin deposit form
    @GetMapping("/admin/deposit/{accountId}")
    public String showAdminDeposit(@PathVariable Long accountId,
                                   Model model,
                                   HttpSession session) {

        User user = (User) session.getAttribute("loggedInUser");

        // allow only admin
        if (user == null || !"ADMIN".equalsIgnoreCase(user.getRole())) {
            return "redirect:/login";
        }

        model.addAttribute("accountId", accountId);

        return "deposit";
    }

    // handle admin deposit
    @PostMapping("/admin/deposit")
    public String adminDeposit(@RequestParam Long accountId,
                               @RequestParam double amount,
                               @RequestParam String password,
                               HttpSession session) {

        User user = (User) session.getAttribute("loggedInUser");

        // allow only admin
        if (user == null || !"ADMIN".equalsIgnoreCase(user.getRole())) {
            return "redirect:/login";
        }

        // check password
        if (!user.getPassword().equals(password)) {
            return "redirect:/transactions/" + accountId + "?error=wrong password";
        }

        boolean success = transactionService.deposit(accountId, amount);

        if (!success) {
            return "redirect:/transactions/" + accountId + "?error=account not found";
        }

        return "redirect:/transactions/" + accountId + "?success=deposit successful";
    }

    // show admin withdraw form
    @GetMapping("/admin/withdraw/{accountId}")
    public String showAdminWithdraw(@PathVariable Long accountId,
                                    Model model,
                                    HttpSession session) {

        User user = (User) session.getAttribute("loggedInUser");

        // allow only admin
        if (user == null || !"ADMIN".equalsIgnoreCase(user.getRole())) {
            return "redirect:/login";
        }

        model.addAttribute("accountId", accountId);

        return "withdraw";
    }

    // handle admin withdraw
    @PostMapping("/admin/withdraw")
    public String adminWithdraw(@RequestParam Long accountId,
                                @RequestParam double amount,
                                @RequestParam String password,
                                HttpSession session) {

        User user = (User) session.getAttribute("loggedInUser");

        // allow only admin
        if (user == null || !"ADMIN".equalsIgnoreCase(user.getRole())) {
            return "redirect:/login";
        }

        // check password
        if (!user.getPassword().equals(password)) {
            return "redirect:/transactions/" + accountId + "?error=wrong password";
        }

        boolean success = transactionService.withdraw(accountId, amount);

        if (!success) {
            return "redirect:/transactions/" + accountId + "?error=insufficient balance or account not found";
        }

        return "redirect:/transactions/" + accountId + "?success=withdraw successful";
    }

    // show transfer page for customer
    @GetMapping("/transfer/{accountId}")
    public String showTransferPage(@PathVariable Long accountId, Model model, HttpSession session) {

        // get logged in user
        User user = (User) session.getAttribute("loggedInUser");

        // allow only customer
        if (user == null || !"CUSTOMER".equalsIgnoreCase(user.getRole())) {
            return "redirect:/login";
        }

        // pass account id to view
        model.addAttribute("accountId", accountId);

        return "transfer";
    }

    // handle transfer for customers only
    @PostMapping("/transfer")
    public String transfer(@RequestParam Long accountId,
                           @RequestParam Long accountNumber,
                           @RequestParam double amount,
                           @RequestParam String password,
                           HttpSession session) {

        User user = (User) session.getAttribute("loggedInUser");

        // allow only customer
        if (user == null || !"CUSTOMER".equalsIgnoreCase(user.getRole())) {
            return "redirect:/login";
        }

        // check password
        if (!user.getPassword().equals(password)) {
            return "redirect:/transactions/" + accountId + "?error=wrong password";
        }

        boolean success = transactionService.transfer(accountId, accountNumber, amount);

        if (!success) {
            return "redirect:/transactions/" + accountId + "?error=transfer failed";
        }

        return "redirect:/transactions/" + accountId + "?success=transfer successful";
    }
}
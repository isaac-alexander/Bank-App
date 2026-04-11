package com.alexander.banking_app.controller;

import com.alexander.banking_app.entity.Transaction;
import com.alexander.banking_app.service.TransactionService;
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

    // show transaction history
    @GetMapping("/{accountId}")
    public String getHistory(@PathVariable Long accountId, Model model) {

        List<Transaction> list = transactionService.getHistory(accountId); // fetch history

        model.addAttribute("transactions", list); // send to html

        model.addAttribute("accountId", accountId); // keep id

        return "transactions"; // transactions.html
    }

    // show deposit form
    @GetMapping("/deposit/{accountId}")
    public String showDepositForm(@PathVariable Long accountId, Model model) {

        model.addAttribute("accountId", accountId); // send account id

        return "deposit"; // deposit.html
    }

    // handle deposit
    @PostMapping("/deposit")
    public String deposit(@RequestParam Long accountId,
                          @RequestParam double amount) {

        transactionService.deposit(accountId, amount); // calls service to deposit money

        // send success message and returns back to transactions page
        return "redirect:/transactions/" + accountId + "?success=" + amount + " has been added to your account";
    }

    // show withdraw form
    @GetMapping("/withdraw/{accountId}")
    public String showWithdrawForm(@PathVariable Long accountId, Model model) {

        model.addAttribute("accountId", accountId);

        return "withdraw"; // withdraw.html
    }

    // handle withdraw
    @PostMapping("/withdraw")
    public String withdraw(@RequestParam Long accountId,
                           @RequestParam double amount) {

        boolean success = transactionService.withdraw(accountId, amount); // calls service to withdraw

        // if withdraw fails
        if (!success) {
            return "redirect:/transactions/" + accountId + "?error=insufficient funds, please check your balance";
        }

        return "redirect:/transactions/" + accountId + "?success=withdraw successful";
    }

}

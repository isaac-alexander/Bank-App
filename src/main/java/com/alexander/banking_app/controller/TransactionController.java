package com.alexander.banking_app.controller;

import com.alexander.banking_app.entity.Account;
import com.alexander.banking_app.entity.Transaction;
import com.alexander.banking_app.entity.User;
import com.alexander.banking_app.repository.AccountRepository;
import com.alexander.banking_app.repository.UserRepository;
import com.alexander.banking_app.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    // method to get current user
    private User getCurrentUser(Authentication authentication) {

        // if not logged in return null
        if (authentication == null) return null;

        // get user from database as optional
        Optional<User> optionalUser = userRepository.findByUsername(authentication.getName());

        // if user not found return null
        return optionalUser.orElse(null);

        // return actual user
    }

    // show customer transaction history
    @GetMapping("/{accountId}")
    public String getHistory(@PathVariable Long accountId,
                             Authentication authentication,
                             Model model) {

        User user = getCurrentUser(authentication);

        if (user == null) {
            return "redirect:/login";
        }

        Account account = accountRepository.findById(accountId)
                .orElse(null);

        if (account == null) {
            return "redirect:/dashboard";
        }

        // CUSTOMER must own the account
        if ("CUSTOMER".equalsIgnoreCase(user.getRole())) {

            if (!account.getUser().getId().equals(user.getId())) {
                return "redirect:/dashboard";
            }
        }

        List<Transaction> transactions =
                transactionService.getHistory(accountId);

        model.addAttribute("transactions", transactions);
        model.addAttribute("accountId", accountId);

        return "transactions";
    }

    // admin all transactions
    @GetMapping("/admin/all")
    public String getAllTransactions(Authentication authentication,
                                     Model model) {

        User user = getCurrentUser(authentication);

        if (user == null || !"ADMIN".equalsIgnoreCase(user.getRole())) {
            return "redirect:/dashboard";
        }

        model.addAttribute("transactions",
                transactionService.getAllTransactions());

        return "admin-transactions";
    }

    // show deposit page
    @GetMapping("/admin/deposit/{accountId}")
    public String showAdminDeposit(@PathVariable Long accountId,
                                   Authentication authentication,
                                   Model model) {

        User user = getCurrentUser(authentication);

        if (user == null || !"ADMIN".equalsIgnoreCase(user.getRole())) {
            return "redirect:/dashboard";
        }

        model.addAttribute("accountId", accountId);

        return "deposit";
    }

    // handle deposit
    @PostMapping("/admin/deposit")
    public String adminDeposit(@RequestParam Long accountId,
                               @RequestParam double amount,
                               Authentication authentication) {

        User user = getCurrentUser(authentication);

        if (user == null || !"ADMIN".equalsIgnoreCase(user.getRole())) {
            return "redirect:/dashboard";
        }

        transactionService.deposit(accountId, amount);

        return "redirect:/dashboard?success=deposit successful";
    }

    // show withdraw page
    @GetMapping("/admin/withdraw/{accountId}")
    public String showAdminWithdraw(@PathVariable Long accountId,
                                    Authentication authentication,
                                    Model model) {

        User user = getCurrentUser(authentication);

        if (user == null || !"ADMIN".equalsIgnoreCase(user.getRole())) {
            return "redirect:/dashboard";
        }

        model.addAttribute("accountId", accountId);

        return "withdraw";
    }

    // handle withdraw
    @PostMapping("/admin/withdraw")
    public String adminWithdraw(@RequestParam Long accountId,
                                @RequestParam double amount,
                                Authentication authentication) {

        User user = getCurrentUser(authentication);

        if (user == null || !"ADMIN".equalsIgnoreCase(user.getRole())) {
            return "redirect:/dashboard";
        }

        transactionService.withdraw(accountId, amount);

        return "redirect:/dashboard?success=withdraw successful";
    }

    // transfer page
    @GetMapping("/transfer/{accountId}")
    public String showTransferPage(@PathVariable Long accountId,
                                   Authentication authentication,
                                   Model model) {

        User user = getCurrentUser(authentication);

        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("accountId", accountId);

        return "transfer";
    }

    // handle transfer
    @PostMapping("/transfer")
    public String transfer(@RequestParam Long accountId,
                           @RequestParam Long accountNumber,
                           @RequestParam double amount,
                           Authentication authentication) {

        User user = getCurrentUser(authentication);

        if (user == null) {
            return "redirect:/login";
        }

        transactionService.transfer(accountId, accountNumber, amount);

        return "redirect:/dashboard?success=transfer successful";
    }
}
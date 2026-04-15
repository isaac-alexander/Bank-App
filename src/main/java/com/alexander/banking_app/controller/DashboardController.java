package com.alexander.banking_app.controller;

import com.alexander.banking_app.dto.UserDto;
import com.alexander.banking_app.entity.Account;
import com.alexander.banking_app.entity.User;
import com.alexander.banking_app.repository.AccountRepository;
import com.alexander.banking_app.repository.UserRepository;
import com.alexander.banking_app.service.TransactionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

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
        User sessionUser = (User) session.getAttribute("loggedInUser");

        if (sessionUser == null) {
            return "redirect:/login";
        }

        // refresh user from database
        Optional<User> userOptional = userRepository.findById(sessionUser.getId());

        if (userOptional.isEmpty()) {
            session.invalidate();
            return "redirect:/login";
        }

        User user = userOptional.get();

        // dto mapping
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setDateOfBirth(user.getDateOfBirth());
        dto.setAddress(user.getAddress());

        model.addAttribute("user", dto);

        Account account = accountRepository.findByUserId(user.getId());

        model.addAttribute("account", account);

        // transactions (only if account exists)
        if (account != null) {
            model.addAttribute(
                    "transactions",
                    transactionService.getHistory(account.getId())
            );
        }

        if ("ADMIN".equalsIgnoreCase(user.getRole())) {
            model.addAttribute("users", userRepository.findAll());
            model.addAttribute("accounts", accountRepository.findAll());
        }

        return "dashboard";
    }
}
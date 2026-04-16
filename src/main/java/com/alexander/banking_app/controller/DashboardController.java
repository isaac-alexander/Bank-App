package com.alexander.banking_app.controller;

import com.alexander.banking_app.dto.UserDto;
import com.alexander.banking_app.entity.Account;
import com.alexander.banking_app.entity.User;
import com.alexander.banking_app.repository.AccountRepository;
import com.alexander.banking_app.repository.UserRepository;
import com.alexander.banking_app.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
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
    public String dashboard(Authentication authentication, Model model) {

        // check login
        if (authentication == null) {
            return "redirect:/login";
        }

        String username = authentication.getName();

        Optional<User> userOptional =
                Optional.ofNullable(userRepository.findByUsername(username));

        if (userOptional.isEmpty()) {
            return "redirect:/login";
        }

        User user = userOptional.get();

        // map user to dto
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setDateOfBirth(user.getDateOfBirth());
        dto.setAddress(user.getAddress());

        model.addAttribute("user", dto);

        Account account = accountRepository.findByUser(user).orElse(null);

        model.addAttribute("account", account);

        if (account != null) {
            model.addAttribute(
                    "transactions",
                    transactionService.getHistory(account.getId())
            );
        }

        // admin data
        if ("ADMIN".equalsIgnoreCase(user.getRole())) {
            model.addAttribute("users", userRepository.findAll());
            model.addAttribute("accounts", accountRepository.findAll());
        }

        return "dashboard";
    }
}
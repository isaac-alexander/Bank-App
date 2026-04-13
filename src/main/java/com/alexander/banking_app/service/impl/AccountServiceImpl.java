package com.alexander.banking_app.service.impl;

import com.alexander.banking_app.entity.Account;
import com.alexander.banking_app.entity.User;
import com.alexander.banking_app.repository.AccountRepository;
import com.alexander.banking_app.repository.UserRepository;
import com.alexander.banking_app.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository; // used to save account

    @Autowired
    private UserRepository userRepository; // used to fetch user

    @Override
    public Account createAccount(Long userId, String type) {

        Account account = new Account(); // create new account object

        // get user from database using id
        User user = userRepository.findById(userId).orElse(null);

        // if user not found
        if (user == null) {
            throw new RuntimeException("user not found");
        }

        // get phone number
        String phone = user.getPhoneNumber();

        // safety check
        if (phone == null || phone.length() < 10) {
            throw new RuntimeException("invalid phone number");
        }

        // remove first digit (08012345678 - 8012345678)
        String accountNumberString = phone.substring(1);

        // convert string to long
        Long accountNumber = Long.parseLong(accountNumberString);

        // check if account type is empty
        if (type == null || type.equals("")) {
            throw new RuntimeException("account type is required");
        }

        // convert to uppercase for consistency
        String normalizedType = type.toUpperCase();

        // get all accounts for this user
        List<Account> existingAccounts = accountRepository.findByUser(user);

        // check if same account type already exists
        for (Account acc : existingAccounts) {

            if (acc.getAccountType() != null &&
                    acc.getAccountType().equalsIgnoreCase(normalizedType)) {

                throw new RuntimeException("account already exists for this type");
            }
        }

        // set values
        account.setAccountNumber(accountNumber); // set generated account number
        account.setUser(user); // link account to user
        account.setAccountType(normalizedType); // savings, checking

        // default balance
        account.setBalance(1000);

        return accountRepository.save(account); // save to database
    }

    @Override
    public List<Account> getUserAccounts(Long userId) {

        // get user first
        User user = userRepository.findById(userId).orElse(null);

        // check if user exists
        if (user == null) {
            throw new RuntimeException("user not found");
        }

        // fetch accounts using user object
        return accountRepository.findByUser(user);
    }
}
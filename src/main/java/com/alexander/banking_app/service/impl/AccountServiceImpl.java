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

        // remove first digit (08012345678 - 8012345678)
        String accountNumberString = phone.substring(1);

        // convert string to long
        Long accountNumber = Long.parseLong(accountNumberString);

        // set values
        account.setAccountNumber(accountNumber); // set generated account number
        account.setUserId(userId); // link to user
        account.setAccountType(type); // savings, checking
        account.setBalance(0); // default balance

        return accountRepository.save(account); // save to database
    }

    @Override
    public List<Account> getUserAccounts(Long userId) {

        return accountRepository.findByUserId(userId); // get all accounts for user
    }
}
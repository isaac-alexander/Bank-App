package com.alexander.banking_app.service.impl;

import com.alexander.banking_app.entity.Account;
import com.alexander.banking_app.entity.Transaction;
import com.alexander.banking_app.repository.AccountRepository;
import com.alexander.banking_app.repository.TransactionRepository;
import com.alexander.banking_app.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    // get transaction history
    @Override
    public List<Transaction> getHistory(Long accountId) {

        return transactionRepository
                .findByAccountIdOrderByDateDesc(accountId);
    }

    // admin get all
    @Override
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    // deposit
    @Override
    public void deposit(Long accountId, double amount) {

        if (amount <= 0) {
            throw new RuntimeException("invalid amount");
        }

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("account not found"));

        account.setBalance(account.getBalance() + amount);

        Transaction t = new Transaction();
        t.setAccountId(accountId);
        t.setAmount(amount);
        t.setType("DEPOSIT");
        t.setDate(LocalDateTime.now());

        accountRepository.save(account);
        transactionRepository.save(t);
    }

    // withdraw
    @Override
    public void withdraw(Long accountId, double amount) {

        if (amount <= 0) {
            throw new RuntimeException("invalid amount");
        }

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("account not found"));

        if (account.getBalance() < amount) {
            throw new RuntimeException("insufficient balance");
        }

        account.setBalance(account.getBalance() - amount);

        Transaction t = new Transaction();
        t.setAccountId(accountId);
        t.setAmount(amount);
        t.setType("WITHDRAW");
        t.setDate(LocalDateTime.now());

        accountRepository.save(account);
        transactionRepository.save(t);
    }

    // transfer
    @Override
    public void transfer(Long fromAccountId,
                         Long toAccountNumber,
                         double amount) {

        if (amount <= 0) {
            throw new RuntimeException("invalid amount");
        }

        Account sender = accountRepository.findById(fromAccountId)
                .orElseThrow(() -> new RuntimeException("sender not found"));

        Account receiver = accountRepository
                .findByAccountNumber(toAccountNumber)
                .orElseThrow(() -> new RuntimeException("receiver not found"));

        if (sender.getBalance() < amount) {
            throw new RuntimeException("insufficient balance");
        }

        // update balances
        sender.setBalance(sender.getBalance() - amount);
        receiver.setBalance(receiver.getBalance() + amount);

        // sender transaction
        Transaction debit = new Transaction();
        debit.setAccountId(sender.getId());
        debit.setAmount(amount);
        debit.setType("TRANSFER_OUT");
        debit.setDate(LocalDateTime.now());

        // receiver transaction
        Transaction credit = new Transaction();
        credit.setAccountId(receiver.getId());
        credit.setAmount(amount);
        credit.setType("TRANSFER_IN");
        credit.setDate(LocalDateTime.now());

        accountRepository.save(sender);
        accountRepository.save(receiver);

        transactionRepository.save(debit);
        transactionRepository.save(credit);
    }
}
package com.alexander.banking_app.service.impl;

import com.alexander.banking_app.entity.Account;
import com.alexander.banking_app.entity.Transaction;
import com.alexander.banking_app.repository.AccountRepository;
import com.alexander.banking_app.repository.TransactionRepository;
import com.alexander.banking_app.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public boolean deposit(Long accountId, double amount) {

        // find account by id
        Account account = accountRepository.findById(accountId).orElse(null);

        // validate account and amount
        if (account == null || amount <= 0) {
            return false;
        }

        // update balance
        account.setBalance(account.getBalance() + amount);

        // save updated account balance
        accountRepository.save(account);

        // create transaction record for deposit
        Transaction transaction = new Transaction();
        transaction.setAccountId(accountId);
        transaction.setAmount(amount);
        transaction.setType("DEPOSIT");
        transaction.setDate(java.time.LocalDateTime.now());

        // save transaction log
        transactionRepository.save(transaction);

        return true;
    }

    @Override
    public boolean withdraw(Long accountId, double amount) {

        // find account by id
        Account account = accountRepository.findById(accountId).orElse(null);

        // validate account and amount
        if (account == null || amount <= 0) {
            return false;
        }

        // check sufficient balance
        if (account.getBalance() < amount) {
            return false;
        }

        // update balance
        account.setBalance(account.getBalance() - amount);

        // save updated account
        accountRepository.save(account);

        // create transaction record for withdraw
        Transaction transaction = new Transaction();
        transaction.setAccountId(accountId);
        transaction.setAmount(amount);
        transaction.setType("WITHDRAW");
        transaction.setDate(java.time.LocalDateTime.now());

        // save transaction log
        transactionRepository.save(transaction);

        return true;
    }

    @Override
    public boolean transfer(Long fromAccountId, Long toAccountNumber, double amount) {

        // find sender account
        Account sender = accountRepository.findById(fromAccountId).orElse(null);

        // find receiver account by account number
        Account receiver = null;

        // find receiver by account number
        for (Account acc : accountRepository.findAll()) {

            if (acc.getAccountNumber() != null &&
                    acc.getAccountNumber().equals(toAccountNumber)) {

                receiver = acc;
                break;
            }
        }

        // validate accounts and amount
        if (sender == null || receiver == null || amount <= 0) {
            return false;
        }

        // check balance
        if (sender.getBalance() < amount) {
            return false;
        }

        // deduct from sender
        sender.setBalance(sender.getBalance() - amount);

        // add to receiver
        receiver.setBalance(receiver.getBalance() + amount);

        // save both accounts
        accountRepository.save(sender);
        accountRepository.save(receiver);

        // create debit transaction for sender
        Transaction debit = new Transaction();
        debit.setAccountId(fromAccountId);
        debit.setAmount(amount);
        debit.setType("TRANSFER_OUT");
        debit.setDate(java.time.LocalDateTime.now());

        transactionRepository.save(debit);

        // create credit transaction for receiver
        Transaction credit = new Transaction();
        credit.setAccountId(receiver.getId());
        credit.setAmount(amount);
        credit.setType("TRANSFER_IN");
        credit.setDate(java.time.LocalDateTime.now());

        transactionRepository.save(credit);

        return true;
    }

    @Override
    public List<Transaction> getHistory(Long accountId) {

        // return all transactions for account
        return transactionRepository.findByAccountId(accountId);
    }
}
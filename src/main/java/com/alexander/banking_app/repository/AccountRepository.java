package com.alexander.banking_app.repository;

import com.alexander.banking_app.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {

    List<Account> findByUserId(Long userId); // get all accounts for user

}

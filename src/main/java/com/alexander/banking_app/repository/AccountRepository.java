package com.alexander.banking_app.repository;

import com.alexander.banking_app.entity.Account;
import com.alexander.banking_app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {

    // get accounts using user object
    List<Account> findByUser(User user);

}

package com.example.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.entity.Account;

import com.example.repository.AccountRepository;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account addAccount(Account account) {
        if(account.getPassword().length() > 4 && account.getUsername().length() > 0) {
            return accountRepository.save(account);
        }
        return null;
    }

    public Account login(Account account) {
        Account logAccount = accountRepository.findByUsername(account.getUsername());
        if(logAccount == null) {
            return null;
        }
        if(logAccount.getPassword().equals(account.getPassword())) {
            return logAccount;
        }
        return null;
    }

    public Account getByName(Account account) {
        return accountRepository.findByUsername(account.getUsername());
    }
}
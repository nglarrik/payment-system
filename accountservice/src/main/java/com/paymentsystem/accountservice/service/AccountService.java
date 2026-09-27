package com.paymentsystem.accountservice.service;

import com.paymentsystem.accountservice.domain.Account;
import com.paymentsystem.accountservice.repository.AccountRepository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Service;


@Service
public class AccountService {
    private final AccountRepository accountRepository;
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
        
    }

    public Account createAccount(String ownerUser, String currency)
    {
        Account account = new Account();
        account.setId(UUID.randomUUID());
        account.setOwnerUser(ownerUser);
        account.setBalance(BigDecimal.ZERO);
        account.setCurrency(currency);

        Instant now = Instant.now();
        account.setUpdatedAt(now);
        account.setCreatedAt(now);


        return accountRepository.save(account);
    }
}

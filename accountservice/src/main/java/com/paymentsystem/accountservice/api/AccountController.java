package com.paymentsystem.accountservice.api;

import com.paymentsystem.accountservice.domain.Account;
import com.paymentsystem.accountservice.service.AccountService;

import java.util.UUID;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/accounts")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService)
    {
        this.accountService = accountService;
    }

    @PostMapping
    public Account createAccount(@RequestParam String ownerUser, @RequestParam String currency)
    {
        return accountService.createAccount(ownerUser, currency);
    }

    @GetMapping("/{id}")
    public Account getAccount(@PathVariable UUID id) {
        return accountService.getAccount(id);
    }
    
}

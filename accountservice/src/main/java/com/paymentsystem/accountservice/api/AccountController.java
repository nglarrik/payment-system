package com.paymentsystem.accountservice.api;

import com.paymentsystem.accountservice.domain.Account;
import com.paymentsystem.accountservice.service.AccountService;
import org.springframework.web.bind.annotation.*;


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
}

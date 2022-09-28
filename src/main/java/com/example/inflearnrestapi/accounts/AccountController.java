package com.example.inflearnrestapi.accounts;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/login")
    public void login(@RequestBody @Validated AccountDto accountDto) {
        Authentication authentication = accountService.login(accountDto);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/signup")
    public Account signup(@RequestBody AccountDto accountDto) {
        return accountService.saveAccount(accountDto);
    }
}

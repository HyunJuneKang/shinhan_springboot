package com.shinhan.bananaapp.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AccountExceptionTestController {
    private final AccountExceptionTestService accountExceptionTestService;
    @GetMapping("/account/ex1/{id}")
    public String f1(@PathVariable Long id){
        return accountExceptionTestService.f1(id);
    }
}

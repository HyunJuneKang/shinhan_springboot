package com.shinhan.bananaapp.section4;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AppRunner implements CommandLineRunner {
    private final AccountService service;
    public AppRunner(AccountService service){
        this.service = service;
    }
    @Override
    public void run(String... args) throws Exception {
        service.transfer(300_000L);
    }
}

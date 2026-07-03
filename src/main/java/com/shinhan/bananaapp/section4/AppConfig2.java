package com.shinhan.bananaapp.section4;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig2 {
    @Bean
    public Notifier notifier2(){
        return new EmailNotifier();
    }
    @Bean
    public AccountService accountService(){
        return new AccountService(notifier2());
    }
}

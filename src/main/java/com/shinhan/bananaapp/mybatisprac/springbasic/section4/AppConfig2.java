package com.shinhan.bananaapp.mybatisprac.springbasic.section4;

import org.springframework.context.annotation.Bean;

//@Configuration
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

package com.shinhan.bananaapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
//@ComponentScan(basePackages = {"com.shinhan.bananaapp","net.firstzone.other"})
public class BananaappApplication {

    public static void main(String[] args) {
        SpringApplication.run(BananaappApplication.class, args);
    }

}

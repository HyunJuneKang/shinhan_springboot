package com.shinhan.bananaapp.di;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration //@SpringBootApplication 에 의해 실행
public class ShinhanConfig {

    public ShinhanConfig(){
        System.out.println("shinhanConfig가 생성함");
    }

    @Bean
    public ShinhanDTO makeDTO(){
        System.out.println("만들어짐");
        return new ShinhanDTO("신한ds","을지로","010-1234-5678");
    }
}

package com.shinhan.bananaapp.di;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration //@SpringBootApplication 에 의해 실행
public class ShinhanConfig {

    public ShinhanConfig(){
    }

    @Bean
    @Primary
    public ShinhanDTO makeDTO(){
        return new ShinhanDTO("신한ds","을지로","010-1234-5678");
    }
    @Bean
    public ShinhanDTO makeDTO2(){
        return new ShinhanDTO("신한ds2","을지로","010-1234-5678");
    }

}

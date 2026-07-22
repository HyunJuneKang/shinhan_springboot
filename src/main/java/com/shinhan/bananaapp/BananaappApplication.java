package com.shinhan.bananaapp;

import jakarta.servlet.Filter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Arrays;

@SpringBootApplication
@EnableAspectJAutoProxy
//@EnableJpaRepositories(basePackages = "com.shinhan.repository")
@EnableJpaAuditing
@EnableScheduling
public class BananaappApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(BananaappApplication.class, args);

        System.out.println("=== 실제 Filter Bean ===");
        String[] filterBeans = context.getBeanNamesForType(Filter.class);
        Arrays.stream(filterBeans).forEach(System.out::println);

        System.out.println("=== FilterRegistrationBean ===");
        String[] registrationBeans = context.getBeanNamesForType(FilterRegistrationBean.class);
        Arrays.stream(registrationBeans).forEach(System.out::println);
    }
}
//sdafasdfdasfdsf
/*
requestContextFilter :
현재 요청 정보를 ThreadLocal에 넣어주는 필터
지금 처리 중인 request/response 정보를
스프링 내부 여러 곳에서 꺼내 쓸 수 있게 해주는 필터

formContentFilter
PUT/PATCH/DELETE 같은 요청에서도 form 데이터를 파라미터처럼 읽게 도와줌

characterEncodingFilter
요청/응답 문자셋을 UTF-8로 맞춰서 한글 깨짐 방지
 */
package com.shinhan.bananaapp.mybatisprac.springbasic.section3;


import org.springframework.context.annotation.Bean;

import java.util.*;

/**
 * ╔══════════════════════════════════════════════════════════════════╗
 * ║  AppConfig — XML을 완전히 대체하는 Java 설정 클래스              ║
 * ╠══════════════════════════════════════════════════════════════════╣
 * ║  @Configuration  : 이 클래스가 Spring 설정 파일임을 선언         ║
 * ║  @ComponentScan  : 지정 패키지에서 @Component 클래스를 자동 탐색  ║
 * ║  @Bean           : 메서드 반환값을 Spring 빈으로 등록             ║
 * ╚══════════════════════════════════════════════════════════════════╝
 * <p>
 * XML 방식과 1:1 비교
 * ─────────────────────────────────────────────────────────────────
 * Before (XML):
 * <context:component-scan base-package="com.demo"/>
 * <bean id="book1" class="com.demo.domain.Book">...</bean>
 * <p>
 * After (Java):
 *
 * @ComponentScan("com.demo")
 * @Bean public Book book1() { ... }
 */
//@Configuration
//@ComponentScan("com.shinhan.bananaapp.section2")    // com.demo 패키지 전체를 스캔
public class AppConfig {

    // ── Book 빈 3개 정의 ────────────────────────────────────────────
    // XML: <bean id="book1" class="...Book"> <property .../> </bean>
    @Bean
    public Book1 book1() {
        return new Book1("Clean Code", 28000, "기술서");
    }

    @Bean
    public Book1 book2() {
        return new Book1("어린왕자", 12000, "소설");
    }

    @Bean
    public Book1 book3() {
        return new Book1("미라클 모닝", 15000, "자기계발");
    }

    // ── List<Book> 빈 — People의 @Qualifier("bookList") 와 연결 ─────
    // XML: <property name="books"><list><ref bean="book1"/>...</list>
    @Bean("bookList")
    public List<Book1> bookList() {
        return Arrays.asList(book1(), book2(), book3());
        // ★ @Bean 메서드를 호출해도 Spring이 싱글톤을 보장 (같은 객체 반환)
    }

    // ── Map<String,Integer> 빈 ─────────────────────────────────────
    // XML: <property name="scores"><map><entry key=.../></map>
    @Bean("scoreMap")
    public Map<String, Integer> scoreMap() {
        Map<String, Integer> map = new LinkedHashMap<>();
        map.put("국어", 95);
        map.put("영어", 88);
        map.put("수학", 72);
        map.put("과학", 91);
        return map;
    }

    // ── Properties 빈 ──────────────────────────────────────────────
    // XML: <property name="contacts"><props><prop key=...>
    @Bean("contactProps")
    public Properties contactProps() {
        Properties props = new Properties();
        props.setProperty("instagram", "@hong_gildong");
        props.setProperty("github", "github.com/honggildong");
        props.setProperty("blog", "https://hong.tistory.com");
        return props;
    }
}

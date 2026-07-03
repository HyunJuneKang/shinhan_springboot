package com.shinhan.bananaapp.section3;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * ╔══════════════════════════════════════════════════════════════╗
 * ║  @Component  →  Spring이 이 클래스를 빈으로 자동 등록        ║
 * ║  @Value      →  application.properties 또는 직접 값 주입    ║
 * ╚══════════════════════════════════════════════════════════════╝
 * <p>
 * XML 방식과 비교:
 * Before: <bean id="book1" class="com.demo.domain.Book">
 * <property name="title" value="Clean Code"/>
 * </bean>
 * After:  @Component + @Value 어노테이션으로 대체
 */
@Component   // id는 기본적으로 클래스명 소문자 → "book"
public class Book1 {
    // @Value: 필드에 직접 값을 주입
    // SpEL(Spring Expression Language) 문법 지원
    @Value("Clean Code")
    private String title;
    @Value("28000")
    private int price;
    @Value("기술서")
    private String kind;

    // 기본 생성자 (Spring이 객체 생성 시 사용)
    public Book1() {
    }

    // 값을 직접 지정하는 생성자 (JavaConfig에서 사용)
    public Book1(String title, int price, String kind) {
        this.title = title;
        this.price = price;
        this.kind = kind;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    @Override
    public String toString() {
        return String.format("Book{ title='%s', price=%,d원, kind='%s' }", title, price, kind);
    }
}

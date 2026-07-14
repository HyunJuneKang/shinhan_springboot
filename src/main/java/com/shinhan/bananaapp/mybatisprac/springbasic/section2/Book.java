package com.shinhan.bananaapp.mybatisprac.springbasic.section2;

import lombok.*;

/**
 * Book - setter injection 예제용
 * XML: <bean id="book1" class="com.demo.domain.Book">
 *        <property name="title" value="Clean Code"/>
 *        <property name="price" value="28000"/>
 *        <property name="kind"  value="기술서"/>
 *      </bean>
 */
@AllArgsConstructor@NoArgsConstructor
@Getter@Setter@ToString

public class Book {
    private String title;
    private int    price;
    private String kind;   // 예) 소설, 기술서, 자기계발

}

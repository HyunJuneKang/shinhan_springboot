package com.shinhan.bananaapp.section1;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TVUser3 {
    public static void main(String[] args) {
        f1();
    }

    private static void f1() {
        ApplicationContext context = new ClassPathXmlApplicationContext("config/section1.xml");
        TV tv = context.getBean("tv",TV.class);
        tv.powerOn();
        tv.powerOff();
    }

}

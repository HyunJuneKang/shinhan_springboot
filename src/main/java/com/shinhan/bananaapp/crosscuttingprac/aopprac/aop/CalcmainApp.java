package com.shinhan.bananaapp.crosscuttingprac.aopprac.aop;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class CalcmainApp {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("config/section3.xml");
        Calculator calc = context.getBean("calculatorImpl",Calculator.class);

        int result = calc.add(5,6);
        System.out.println(result);
    }
}

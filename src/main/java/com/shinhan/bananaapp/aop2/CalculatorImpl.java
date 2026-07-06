package com.shinhan.bananaapp.aop2;

import org.springframework.stereotype.Component;

//핵심로직,주관심사,Target
@Component
public class CalculatorImpl implements Calculator {

    @Override
    public int add(int a, int b) {
        System.out.println("기존 로직 수행함");
        return a+b;
    }

    @Override
    public int add(int a, int b, int c) {
        System.out.println("기존 로직 수행함");
        return a+b+c;
    }

    @Override
    public int subtract(int a, int b) {
        System.out.println("기존 로직 수행함");
        return a-b;
    }

    @Override
    public int multiply(int a, int b) {
        System.out.println("기존 로직 수행함");
        return a*b;
    }

    @Override
    public int divide(int a, int b) {
        System.out.println("기존 로직 수행함");
        return a/b;
    }
}

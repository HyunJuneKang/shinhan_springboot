package com.shinhan.bananaapp.acp;

//핵심로직,주관심사,Target
public class CalculatorImpl implements Calculator{

    @Override
    public int add(int a, int b) {
        return a+b;
    }

    @Override
    public int add(int a, int b, int c) {
        return a+b+c;
    }

    @Override
    public int subtract(int a, int b) {
        return a-b;
    }

    @Override
    public int multiply(int a, int b) {
        return a*b;
    }

    @Override
    public int divide(int a, int b) {
        return a/b;
    }
}

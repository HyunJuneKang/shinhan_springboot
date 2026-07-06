package com.shinhan.bananaapp.aop2;

import org.springframework.stereotype.Component;

@Component
public class CalcmainApp2 {
    CalculatorImpl calc;
    public CalcmainApp2(CalculatorImpl calc){
        this.calc = calc;
    }

    public void run(String... args) throws Exception {
        int result = calc.add(5,6);
        System.out.println(result);
    }
}

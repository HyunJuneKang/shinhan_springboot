package com.shinhan.bananaapp.section1;

public class TVUser1 {
    public static void main(String[] args) {
        f1();
    }

    private static void f1() {
        //interface pattern, 의존관계에 있다 , 결합높다
        TV tv = new SamsungTV();
        tv.powerOn();
        tv.powerOff();
    }
}

package com.shinhan.bananaapp.mybatisprac.springbasic.section1;

public class TVUser2 {
    public static void main(String[] args) {
        f1();
    }

    private static void f1() {
        TV tv = TVFactory.makeTV("LG");
        tv.powerOn();
        tv.powerOff();
    }

}

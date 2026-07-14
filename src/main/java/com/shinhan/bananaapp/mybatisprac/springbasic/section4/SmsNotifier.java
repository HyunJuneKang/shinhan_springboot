package com.shinhan.bananaapp.mybatisprac.springbasic.section4;

public class SmsNotifier implements Notifier {

    @Override
    public void send(String message) {
        System.out.println("[SMS]" + message);
    }
}

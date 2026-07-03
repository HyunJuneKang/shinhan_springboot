package com.shinhan.bananaapp.section4;

public class EmailNotifier implements Notifier{

    @Override
    public void send(String message) {
        System.out.println("[EMAIL] " + message);
    }
}

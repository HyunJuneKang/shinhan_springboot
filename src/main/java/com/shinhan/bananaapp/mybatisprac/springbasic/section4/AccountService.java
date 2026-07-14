package com.shinhan.bananaapp.mybatisprac.springbasic.section4;

public class AccountService {
    Notifier notifier;
    AccountService(Notifier notifier){//외부에서 주입
        this.notifier = notifier;
    }
    public void transfer(Long amount){
        notifier.send("이체완료: "+amount+ "원");
    }
}

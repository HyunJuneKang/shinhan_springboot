package com.shinhan.bananaapp.mybatisprac.springbasic.section5;

import com.shinhan.bananaapp.mybatisprac.prev.AccountDTO;

//@Repository("empRepo")
public class EmpRepository {
    public AccountDTO getData(){
        AccountDTO acc = AccountDTO.builder()
                .accountNo("1234")
                .ownerName("고대동")
                .balance(1000L)
                .accountType("적금")
                .build();
        return acc;
    }
}

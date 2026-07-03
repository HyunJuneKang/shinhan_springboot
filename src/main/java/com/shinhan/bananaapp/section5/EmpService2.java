package com.shinhan.bananaapp.section5;

import com.shinhan.bananaapp.dto.AccountDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

//@Component + 서비스기능
@Service
@Slf4j
@RequiredArgsConstructor
public class EmpService2 implements EmpServiceInterface{
    //1.field주입

    //2.생성자를 통해 주입
    final EmpRepository repo;
    public AccountDTO selectService(){
        log.info("-----EmpService2----");
        AccountDTO acc = repo.getData();
        return acc;
    }
}

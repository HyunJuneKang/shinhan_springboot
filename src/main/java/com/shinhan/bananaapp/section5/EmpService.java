package com.shinhan.bananaapp.section5;

import com.shinhan.bananaapp.dto.AccountDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

//@Component + 서비스기능
@Service
@RequiredArgsConstructor
public class EmpService {
    //1.field주입

    //2.생성자를 통해 주입
    final EmpRepository repo;
    public AccountDTO selectService(){
        AccountDTO acc = repo.getData();
        return acc;
    }
}

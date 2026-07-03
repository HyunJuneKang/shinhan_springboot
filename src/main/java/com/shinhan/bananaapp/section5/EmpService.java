package com.shinhan.bananaapp.section5;

import com.shinhan.bananaapp.dto.AccountDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
//@Component + 서비스기능
@Service
public class EmpService {
    //1.field주입
    @Autowired
    EmpRepository repo;
    public AccountDTO selectService(){
        AccountDTO acc = repo.getData();
        return acc;
    }
}

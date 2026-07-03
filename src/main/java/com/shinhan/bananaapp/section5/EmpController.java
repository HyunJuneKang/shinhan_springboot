package com.shinhan.bananaapp.section5;

import com.shinhan.bananaapp.dto.AccountDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmpController {
    //1.field를 이용해서 Component 주입
    @Autowired
    EmpService empService;
    @GetMapping("/acc")
    public AccountDTO selectData(){
        return empService.selectService();
    }
}

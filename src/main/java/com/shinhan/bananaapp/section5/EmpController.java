package com.shinhan.bananaapp.section5;

import com.shinhan.bananaapp.dto.AccountDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EmpController {
    //1.field를 이용해서 Component 주입 (DI,IoC )
    //@Autowired
    //2.생성자를 통해서 Injection
    final EmpService empService;
    @GetMapping("/acc")
    public AccountDTO selectData(){
        return empService.selectService();
    }
}

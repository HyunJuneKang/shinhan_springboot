package com.shinhan.bananaapp.mybatisprac.springbasic.section5;

import com.shinhan.bananaapp.mybatisprac.prev.AccountDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;

//@RestController
//@RequiredArgsConstructor
public class EmpController {
    //1.field를 이용해서 Component 주입 (DI,IoC )
    //@Autowired
    //2.생성자를 통해서 Injection
    EmpServiceInterface empS;
    public EmpController(@Qualifier("empService2") EmpServiceInterface empS){
        this.empS = empS;
    }
    @GetMapping("/acc")
    public AccountDTO selectData(){
        return empS.selectService();
    }
}

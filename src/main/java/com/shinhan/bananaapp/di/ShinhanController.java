package com.shinhan.bananaapp.di;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

//Controller는 요청을 받고 응답을 template으로

@Controller
@ResponseBody //@RestController = @Controller + @ResponseBody , response.getWriter().append()
public class ShinhanController {
    @Autowired
    ShinhanService sService;
    @GetMapping("/hello2")
    public String f_1(){
        return sService.getRepo().dto.toString();
    }
}

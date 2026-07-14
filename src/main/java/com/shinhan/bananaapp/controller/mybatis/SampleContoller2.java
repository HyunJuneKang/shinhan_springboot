package com.shinhan.bananaapp.controller.mybatis;

import com.shinhan.bananaapp.property.ShinhanProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleContoller2 {

    @Autowired
    ShinhanProperties props;

    @Value("${shinhan.api.key}")
    String myKey1;

    @GetMapping("sample1")
    public String f_sample1(){
        return "OK" + myKey1;
    }
    @GetMapping("sample2")
    public String f_sample2(){
        return "OK" +
                "\nAPI3: "+props.getApi3() +
                "\nAPI2 Key1 : "+props.getApi2().getKey1() +
                "\nAPI1 Key1 : "+props.getApi1().getKey1();
    }
}

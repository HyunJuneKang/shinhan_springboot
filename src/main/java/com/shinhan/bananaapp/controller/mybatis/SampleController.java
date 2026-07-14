package com.shinhan.bananaapp.controller.mybatis;

import com.shinhan.bananaapp.dto.prev.CarDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//Spring - POJO(Plain Old Java Object)
@RestController
@Slf4j
public class SampleController {

    @GetMapping("/car")
    public CarDTO f2(){
        return CarDTO
                .builder()
                .model("ABC")
                .price(200)
                .build();
    }
    @GetMapping("/carlist")
    public List<CarDTO> f3(){
        return List.of(
                CarDTO.builder()
                        .model("ABC")
                        .price(200)
                        .build(),
                CarDTO.builder()
                        .model("DDD")
                        .price(100)
                        .build()
        );
    }
    @GetMapping("/hello")
    public String f1() {
        System.out.println("====로그수준 test중=====");
        log.trace("TRACE 레벨 로그");
        log.debug("DEBUG 레벨 로그");
        log.info("INFO 레벨 로그");
        log.warn("WARN 레벨 로그");
        log.error("ERROR 레벨 로그");
        return "Hello~~~하이";
    }


}

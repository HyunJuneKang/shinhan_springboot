package com.shinhan.bananaapp.di;

import jakarta.annotation.PostConstruct;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

//@Component <-----@Repo , @Service , @Controller
//@Component는 나의소스이므로 가능 , 다른 라이브러리의 Bean을 사용하려면XML 방식 또는 @Configuration
@Component
@ToString
public class ShinhanRepository {

    //2.field를 통해서 넣기
    ShinhanDTO dto;

    //1.생성자를 통해 injection(DI)
    public ShinhanRepository(@Qualifier("makeDTO") ShinhanDTO dto){
        this.dto = dto;
        System.out.println("ShinhanRepo 생성함" + dto);
    }
    @PostConstruct
    public void init(){
        System.out.println(dto);
    }
}

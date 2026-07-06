package com.shinhan.bananaapp.di;

import org.springframework.stereotype.Component;
//@Component <-----@Repo , @Service , @Controller
//@Component는 나의소스이므로 가능 , 다른 라이브러리의 Bean을 사용하려면XML 방식 또는 @Configuration
@Component
public class ShinhanRepository {
    public ShinhanRepository(){
        System.out.println("ShinhanRepo 생성함");
    }
}

package com.shinhan.bananaapp.di;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@Getter
public class ShinhanService {

    private final ShinhanRepository repo;

    public ShinhanService(ShinhanRepository repo){
        this.repo = repo;
        String result = repo.toString();
        System.out.println("ShinhanService 생성함" + result);
    }
}

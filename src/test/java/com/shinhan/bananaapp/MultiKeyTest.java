package com.shinhan.bananaapp;

import com.shinhan.bananaapp.multikey.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class MultiKeyTest {
    @Autowired
    MultiKeyRepository multiKeyRepository;
    @Autowired
    MultiKeyBRepository multiKeyBRepository;


    @Test
    void f2() {
        MultiKeyBEntity b1 = MultiKeyBEntity.builder()
                .id(MultiKeyB.builder().id1(1).id2(2).build())
                .name("홍길동")
                .phone("010-1234-5678")
                .build();
        MultiKeyBEntity b2 = MultiKeyBEntity.builder()
                .id(MultiKeyB.builder().id1(1).id2(1).build())
                .name("zz")
                .phone("010-3423-5678")
                .build();
        multiKeyBRepository.save(b1);
        multiKeyBRepository.save(b2);
    }

    @Transactional
    @Commit
    @Test
    void f1(){
        MultiKeyEntity entity = MultiKeyEntity
                .builder()
                .id1(1)
                .id2(1)
                .name("kim")
                .phone("010-1234-5678")
                .build();
        MultiKeyEntity entity2 = MultiKeyEntity
                .builder()
                .id1(1)
                .id2(2)
                .name("kim")
                .phone("010-1234-5678")
                .build();
        multiKeyRepository.save(entity);
        multiKeyRepository.save(entity2);
    }
}

package com.shinhan.bananaapp.jpa;

import com.shinhan.bananaapp.jpaprac.entity.entity1.SampleEntity;
import com.shinhan.bananaapp.jpaprac.repository.SampleRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.stream.IntStream;
import java.util.stream.Stream;

// 단위 테스트 = JUnit 사용
@Slf4j
@SpringBootTest
public class SimpleTest {
    @Autowired
    SampleRepository sampleRepo;

    @Test
    void f1() {
        SampleEntity ent1 = SampleEntity.builder()
                .userName("홍길동")
                .name("신한")
                .email("hong@daum.net")
                .amount(new BigDecimal("1000.12"))
                .build();

        SampleEntity savedEntity = sampleRepo.save(ent1);

        System.out.println(savedEntity);
    }
    @Test
    void f2() {
        long time = System.currentTimeMillis();

        IntStream.rangeClosed(1, 10).forEach(i -> {
            SampleEntity ent1 = SampleEntity.builder()
                    .userName("홍길동" + i)
                    .name("신한")
                    .email("hong" + time + i + "@daum.net")
                    .amount(new BigDecimal("1000.12"))
                    .build();

            sampleRepo.save(ent1);
        });
    }
    @Test
    void f3(){
//        sampleRepo.findAll().forEach(System.out::println);
        sampleRepo.findById(7).ifPresent(sample->{
            sample.setAmount(new BigDecimal(2000));
            sample.setName("Jin");
            sample.setEmail("zz");
            sampleRepo.save(sample);
        });
//        System.out.println("건수: " + sampleRepo.count());
    }
    @Test
    void f_work(){
        Class<?> cls = sampleRepo.getClass();
        log.info("repository 이름: {}", cls.getName());
        Stream.of(cls.getInterfaces()).forEach(
                (i)->log.info("interface: "+ i.getName()));
    }
    //쓰기 지연 : 영속성 Context에 있는 데이터 수정 , DB에는 Transaction이 종료되면 반영
    @Test
    @Commit //서비스에서는 불필요, Test에서는 저장원하면 필요
    @Transactional
    void f_update(){
        sampleRepo.findById(10).ifPresent(sample->{
            sample.setName("----신한------");
        });
    }

    @Transactional
    @Test
    void f_select(){
        SampleEntity ent1 = sampleRepo.findById(10).orElse(null);
        SampleEntity ent2 = sampleRepo.findById(10).orElse(null);
        System.out.println(ent1 == ent2); //같다면 1번만
    }
}

/*
bno ------PK
title
content
writer
regDATE
updateDate

CRUD 작업하기위함
Entity 생성
Repository 생성
Junit Test하기위한 Class 생성

 */
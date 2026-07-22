package com.shinhan.bananaapp.batch;

import com.shinhan.bananaapp.batch.batchprac1.entity.BeforeEntity;
import com.shinhan.bananaapp.batch.batchprac1.repository.BeforeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class BatchTest {

    @Autowired
    private BeforeRepository beforeRepository;

    @Test
    void insertBeforeData() {


        List<BeforeEntity> data = List.of(
                        "hong", "kim", "lee", "park", "choi",
                        "testUser1", "testUser2", "jung", "yoon", "shin"
                ).stream()
                .map(name -> BeforeEntity.builder()
                        .name(name)
                        .build())
                .toList();

        beforeRepository.saveAll(data);
    }

}
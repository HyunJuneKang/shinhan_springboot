package com.shinhan.bananaapp.batch.batchprac2.controller;

import com.shinhan.bananaapp.batch.batchprac2.entity.TransactionEntity;
import com.shinhan.bananaapp.batch.batchprac2.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class TestDataController {

    private final TransactionRepository transactionRepository;

    // GET /test/transaction
    @GetMapping("/test/transaction")
    public ResponseEntity<?> addTestData() {

        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);

        List<TransactionEntity> list = List.of(
                TransactionEntity.builder()
                        .accountNo("110-001")
                        .txType("DEPOSIT")
                        .amount(1000000L)
                        .txDate(yesterday)
                        .settled(false)
                        .build(),
                TransactionEntity.builder()
                        .accountNo("110-002")
                        .txType("WITHDRAW")
                        .amount(500000L)
                        .txDate(yesterday)
                        .settled(false)
                        .build(),
                TransactionEntity.builder()
                        .accountNo("110-003")
                        .txType("DEPOSIT")
                        .amount(0L)       // 금액 0 → 필터 확인용
                        .txDate(yesterday)
                        .settled(false)
                        .build()
        );

        transactionRepository.saveAll(list);
        return ResponseEntity.ok("테스트 데이터 " + list.size() + "건 추가");
    }
}

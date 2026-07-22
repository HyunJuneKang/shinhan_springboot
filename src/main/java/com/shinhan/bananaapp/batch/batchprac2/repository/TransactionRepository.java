package com.shinhan.bananaapp.batch.batchprac2.repository;

import com.shinhan.bananaapp.batch.batchprac2.entity.TransactionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;

public interface TransactionRepository
        extends JpaRepository<TransactionEntity, Long> {

    // Reader용 — 미정산 + 날짜 범위 페이징
    Page<TransactionEntity> findBySettledFalseAndTxDateBetween(
            LocalDateTime start, LocalDateTime end, Pageable pageable);

    // Job 완료 후 settled = true 일괄 처리
    @Modifying
    @Query("UPDATE TransactionEntity t SET t.settled = true " +
            "WHERE t.settled = false AND t.txDate BETWEEN :start AND :end")
    int markAsSettled(@Param("start") LocalDateTime start,
                      @Param("end")   LocalDateTime end);
}

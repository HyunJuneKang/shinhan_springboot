package com.shinhan.bananaapp.batch.batchprac3.repository;

import com.shinhan.bananaapp.batch.batchprac3.entity.SavingsMaturityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface SavingsMaturityRepository
        extends JpaRepository<SavingsMaturityEntity, Long> {

    // afterJob — 처리 건수
    int countByMaturityDate(LocalDate date);

    // afterJob — 이자 합계
    @Query("SELECT COALESCE(SUM(s.interest), 0) " +
            "FROM SavingsMaturityEntity s " +
            "WHERE s.maturityDate = :date")
    Long sumInterestByMaturityDate(@Param("date") LocalDate date);

    // afterJob — 원리금 합계
    @Query("SELECT COALESCE(SUM(s.totalAmount), 0) " +
            "FROM SavingsMaturityEntity s " +
            "WHERE s.maturityDate = :date")
    Long sumTotalAmountByMaturityDate(@Param("date") LocalDate date);
}

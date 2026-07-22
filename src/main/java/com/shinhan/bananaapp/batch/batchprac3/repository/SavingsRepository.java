package com.shinhan.bananaapp.batch.batchprac3.repository;

import com.shinhan.bananaapp.batch.batchprac3.entity.SavingsEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface SavingsRepository
        extends JpaRepository<SavingsEntity, Long> {

    // Reader용 — 오늘 만기 + ACTIVE 상태 페이징
    Page<SavingsEntity> findByMaturityDateAndStatus(
            LocalDate maturityDate, String status, Pageable pageable);

    // beforeJob 확인용 — 처리 대상 건수
    int countByMaturityDateAndStatus(
            LocalDate maturityDate, String status);

    // afterJob — ACTIVE → CLOSED 일괄 변경
    @Modifying
    @Query("UPDATE SavingsEntity s SET s.status = 'CLOSED' " +
            "WHERE s.maturityDate = :date AND s.status = 'ACTIVE'")
    int closeMaturedSavings(@Param("date") LocalDate date);
}

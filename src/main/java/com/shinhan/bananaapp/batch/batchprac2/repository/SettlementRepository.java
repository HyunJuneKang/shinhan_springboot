package com.shinhan.bananaapp.batch.batchprac2.repository;

import com.shinhan.bananaapp.batch.batchprac2.entity.SettlementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface SettlementRepository
        extends JpaRepository<SettlementEntity, Long> {

    long countBySettlementDate(LocalDate date);

    //ORACLE: nvl() m nvl2() , COALESCE(null,10,20)
    @Query("SELECT COALESCE(SUM(s.fee), 0) FROM SettlementEntity s " +
            "WHERE s.settlementDate = :date")
    Long sumFeeBySettlementDate(@Param("date") LocalDate date);
}

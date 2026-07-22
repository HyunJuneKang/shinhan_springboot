package com.shinhan.bananaapp.batch.batchprac2.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.*;

@Entity @Table(name = "tbl_settlement")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SettlementEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long          id;
    private String        accountNo;
    private String        txType;
    private Long          amount;
    private Long          fee;               // Processor 계산
    private LocalDate     settlementDate;    // 정산 기준일
    @CreationTimestamp
    private LocalDateTime createdAt;
}

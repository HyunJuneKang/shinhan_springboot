package com.shinhan.bananaapp.batch.batchprac3.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity @Table(name = "tbl_savings_maturity")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SavingsMaturityEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String     savingsNo;      // 적금 번호
    private String     ownerName;      // 가입자
    private String     linkedAccount;  // 입금 계좌
    private Long       principal;      // 원금
    private Long       interest;       // 이자
    private Long       totalAmount;    // 원금 + 이자
    private BigDecimal annualRate;     // 적용 이율
    private LocalDate  maturityDate;   // 만기일
    private LocalDateTime processedAt; // 처리 일시
}

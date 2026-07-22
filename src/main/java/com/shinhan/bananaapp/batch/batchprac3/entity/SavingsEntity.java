package com.shinhan.bananaapp.batch.batchprac3.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity @Table(name = "tbl_savings")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SavingsEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String  savingsNo;      // 적금 번호

    @Column(nullable = false, length = 50)
    private String  ownerName;      // 가입자

    @Column(nullable = false, length = 50)
    private String  linkedAccount;  // 연결 계좌

    @Column(nullable = false)
    private Long    principal;      // 원금

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal annualRate;  // 연이율

    @Column(nullable = false)
    private Long    monthlyAmount;  // 월 납입금

    @Column(nullable = false)
    private int     months;         // 납입 개월

    @Column(nullable = false)
    private LocalDate startDate;    // 가입일

    @Column(nullable = false)
    private LocalDate maturityDate; // 만기일

    @Column(nullable = false, length = 20)
    @Builder.Default
    private String status = "ACTIVE"; // ACTIVE / CLOSED

    private LocalDateTime createdAt;
}

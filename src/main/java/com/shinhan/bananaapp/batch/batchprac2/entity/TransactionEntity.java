package com.shinhan.bananaapp.batch.batchprac2.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity @Table(name = "tbl_transaction")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TransactionEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long          id;
    private String        accountNo;       // 계좌번호
    private String        txType;          // DEPOSIT / WITHDRAW
    private Long          amount;          // 거래금액
    private LocalDateTime txDate;          // 거래일시
    @Builder.Default
    private boolean       settled = false; // 정산 여부
}

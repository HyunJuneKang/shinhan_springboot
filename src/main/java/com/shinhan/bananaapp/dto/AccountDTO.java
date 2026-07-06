package com.shinhan.bananaapp.dto;

import lombok.*;

import java.time.LocalDate;

@Getter@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AccountDTO {
    private Long id;            // PK
    private String accountNo;   // 계좌번호
    private String ownerName;   // 예금주
    private Long balance;       // 잔액
    private String accountType; // SAVINGS / CHECKING
    private LocalDate createAt; // 생성일
}

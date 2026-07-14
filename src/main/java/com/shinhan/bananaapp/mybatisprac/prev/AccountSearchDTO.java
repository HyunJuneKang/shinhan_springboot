package com.shinhan.bananaapp.mybatisprac.prev;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class AccountSearchDTO {
    private String ownerName;    // 예금주 검색어
    private String accountType;  // 계좌 유형 필터
    private Long minBalance;     // 최소 잔액
    private Long maxBalance;     // 최대 잔액
    private String sortBy;       // 정렬 기준
}


package com.shinhan.bananaapp.mybatisprac.prev;

import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    private LocalDate createdAt; // 생성일

    @Builder.Default
    private List<AttachmentDTO> attachments = new ArrayList<>();

    //Builder.default가 없으면 AccountDTO dto = AccountDTO.builder().build();
    //이때 getAttachments()가 null이므로 NullPointerException이 발생합니다.
}

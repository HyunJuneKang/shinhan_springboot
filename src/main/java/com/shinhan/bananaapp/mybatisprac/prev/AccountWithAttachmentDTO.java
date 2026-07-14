package com.shinhan.bananaapp.mybatisprac.prev;

import lombok.*;

/**
 * Flat ResultMap 전용 DTO
 *
 * 계좌 + 첨부파일 컬럼을 한 DTO에 flat하게 담습니다.
 * JOIN 결과: 계좌 1건에 첨부 2건이면 → 행이 2개 중복됩니다.
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Builder @ToString
public class AccountWithAttachmentDTO {

    //flat의 단점은 JOIN 결과: 계좌 1건에 첨부 2건이면 → 행이 2개 중복됩니다.
    // ── account 컬럼 ──────────────────────────────────
    private Long   id;
    private String accountNo;
    private String ownerName;
    private Long   balance;
    private String accountType;

    // ── attachment 컬럼 (flat) ────────────────────────
    private Long   attachmentId;       // null 가능 (첨부 없는 계좌)
    private String originalFilename;
    private String savedFilename;
    private Long   fileSize;
    private String fileType;
}

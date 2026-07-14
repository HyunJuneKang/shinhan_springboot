package com.shinhan.bananaapp.mybatisprac.prev;

import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Builder @ToString
public class AttachmentDTO {
    private Long          id;
    private Long          accountId;
    private String        originalFilename;  // 원본 파일명
    private String        savedFilename;     // UUID 저장명
    private Long          fileSize;          // 파일 크기 (byte)
    private String        fileType;          // 확장자
    private LocalDateTime createdAt;
}

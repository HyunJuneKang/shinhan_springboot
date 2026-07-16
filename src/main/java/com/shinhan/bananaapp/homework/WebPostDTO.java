package com.shinhan.bananaapp.homework;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class WebPostDTO {
    private Long   id;
    private String title;
    private String content;
    private String writer;
    private String writerId;
    private int    viewCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<WebCommentDTO> comments;   // 댓글 목록 포함
    private int commentCount;
}
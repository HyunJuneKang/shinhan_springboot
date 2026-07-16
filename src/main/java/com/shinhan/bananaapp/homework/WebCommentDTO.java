package com.shinhan.bananaapp.homework;

import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class WebCommentDTO {
    private Long   id;
    private String content;
    private String writer;
    private String writerId;
    private Long   postId;
    private LocalDateTime createdAt;
}
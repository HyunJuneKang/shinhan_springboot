package com.shinhan.bananaapp.replyprac.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FreeReplyDTO   {
    private Long rno;
    private String reply;
    private String replyer;
    // FreeReplyEntity.board(FK 주인 객체)를 DTO까지 그대로 노출하지 않고
    // 게시글 번호(bno)만 보관한다. (엔티티 노출 방지 + 순환참조 방지)
    private Long bno;
}


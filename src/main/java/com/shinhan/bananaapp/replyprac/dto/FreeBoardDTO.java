package com.shinhan.bananaapp.replyprac.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FreeBoardDTO   {
    private Long bno;
    private String title;
    private String content;
    private String writer;

    @Builder.Default
    private List<FreeReplyDTO> replyList = new ArrayList<>();

    // 댓글 개수 (목록 화면에서 매번 replyList.size() 계산 안 해도 되도록 미리 세팅)
    private Long replyCount;

}

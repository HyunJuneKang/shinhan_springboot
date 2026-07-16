package com.shinhan.bananaapp.replyprac.entity;

import com.shinhan.bananaapp.jpaprac.entity.entity2.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "t_freereply")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "board")         // 무한루프 방지 필수!
public class FreeReplyEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rno;
    private String reply;
    private String replyer;

    // FK 실제 관리 — board_bno 컬럼 자동 생성
    @ManyToOne(fetch = FetchType.LAZY)
    private FreeBoardEntity board;
}

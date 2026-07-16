package com.shinhan.bananaapp.replyprac.entity;

import com.shinhan.bananaapp.jpaprac.entity.entity2.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "t_freeboard")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "replyList")    // 무한루프 방지 필수!
public class FreeBoardEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno;
    private String title;
    private String content;
    private String writer;

    // mappedBy = FreeReplyEntity의 필드명 "board"
    @OneToMany(mappedBy = "board",
            cascade  = CascadeType.ALL,
            fetch    = FetchType.LAZY)
    private List<FreeReplyEntity> replyList = new ArrayList<>();
}

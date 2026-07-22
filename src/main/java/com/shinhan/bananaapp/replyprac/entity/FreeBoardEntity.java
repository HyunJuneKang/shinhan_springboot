package com.shinhan.bananaapp.replyprac.entity;

import com.shinhan.bananaapp.jpaprac.entity.entity2.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tbl_freeboard")
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
    //mappedBy : 메여있다. 참조하는 테이블의 칼럼이 결정한다.

    @BatchSize(size = 10)
    @OneToMany(mappedBy = "board",
            cascade  = CascadeType.ALL,
            fetch    = FetchType.LAZY)
    private List<FreeReplyEntity> replyList = new ArrayList<>();
}


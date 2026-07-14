package com.shinhan.bananaapp.jpaprac.entity.entity2;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tbl_webboard")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "replies")   // 무한루프 방지
public class WebBoardEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno;
    private String title;
    private String content;
    private String writer;
}

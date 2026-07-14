package com.shinhan.bananaapp.jpaprac.entity.entity1;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
@Builder
@Entity
@Setter@Getter
@Table(name = "board")
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BoardEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String writer;
    @Column(nullable = false)
    private String content;
    @Column(updatable = false)//수정불가
    @CreationTimestamp //생성시
    private LocalDate regDate;
    @UpdateTimestamp //수정시
    private LocalDate updateDate;
}

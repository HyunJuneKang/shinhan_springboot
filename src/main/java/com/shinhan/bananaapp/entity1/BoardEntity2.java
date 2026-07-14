package com.shinhan.bananaapp.entity1;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;

@Builder
@Entity
@Setter@Getter
@Table(name = "board2")
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BoardEntity2 {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    Long bno;
    @Column(nullable = false)
    String title;
    @Column(nullable = false)
    String writer;
    @Column(nullable = false)
    String content;
    @CreationTimestamp
    LocalDate regDate;
    @UpdateTimestamp
    LocalDate updateDate;
}

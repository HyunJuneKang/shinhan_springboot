package com.shinhan.bananaapp.jpaprac.dto;

import lombok.*;

import java.time.LocalDate;

@Builder
@Setter@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Board2DTOMapping {
    private Long bno;
    private String title;
    private String writer;
    private String content;
    private LocalDate regDate;
    private LocalDate updateDate;
}

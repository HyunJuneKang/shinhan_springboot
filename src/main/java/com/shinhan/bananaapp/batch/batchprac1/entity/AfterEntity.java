package com.shinhan.bananaapp.batch.batchprac1.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "tbl_after")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AfterEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 100)
    private String name;
}

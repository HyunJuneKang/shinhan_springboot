package com.shinhan.bananaapp.onetomany.entity;

import com.shinhan.bananaapp.jpaprac.entity.entity2.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@Builder @NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_pdsfiles")
public class PDSFileEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fno;
    private String pdsfilename;
    // pdsno FK는 @JoinColumn이 PDSBoard 쪽에서 관리
}
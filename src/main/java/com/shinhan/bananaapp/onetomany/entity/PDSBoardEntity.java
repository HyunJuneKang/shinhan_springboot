package com.shinhan.bananaapp.onetomany.entity;

import com.shinhan.bananaapp.jpaprac.entity.entity2.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString(exclude = "files2")
@Builder @NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_pdsboard")
public class PDSBoardEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pid;
    private String pname;
    private String pwriter;
    //orphanRemoval true : 고아는 지우기 (부모가 참조하지않는 data는 )
//    @BatchSize(size = 10) //where pdsno in (?,?,?,?)
    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "pdsno") // t_pdsfiles.pdsno FK
    //@JoinColumn이 생략되면 중간 table이 생성된다. tbl_pdsboard_files2 ( t_pdsboard PK)
    List<PDSFileEntity> files2 = new ArrayList<>();
}

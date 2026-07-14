package com.shinhan.bananaapp.jpaprac.entity.entity1;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;

/*
    Entity설계하기 : JPA가 관리하는 대상 Entity라고 한다.
    DDL-AUTO : create <-- 라고 설정했다면 실행 시 자동으로 테이블이 생성됨
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString@Builder

@Entity
@Table(name = "tbl_sample")
public class SampleEntity {
    @Id //PK(primary key)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @Column(name = "user_name")          // 컬럼명 직접 지정
    private String userName;

    @Column(nullable = false)             // NOT NULL
    private String name;

    @Column(unique = true)                // UNIQUE
    private String email;

    @Column(length = 50)                  // VARCHAR(50)
    private String title;

    @Column(updatable = false)            // UPDATE 시 변경 안 됨
    private LocalDate createdDate;

    @Column(precision = 10, scale = 2)   // DECIMAL(10,2)
    private BigDecimal amount;

    @CreationTimestamp                    // INSERT 시 자동 설정
    private Timestamp regDate;

    @UpdateTimestamp                      // UPDATE 시 자동 설정
    private Timestamp updateDate;
}


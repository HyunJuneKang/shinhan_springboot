package com.shinhan.bananaapp.manytoonereview.entity;

import com.shinhan.bananaapp.jpaprac.entity.entity2.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

//JPA annotation
//@Entity : JPA 관리 대상 , EntityManager가 관리한다. DB table에 자동 생성
@Entity @Table(name = "tbl_product")
//Lombok --- 개발환경에서 자동으로 method를 생성
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder@ToString
public class ProductEntity extends BaseEntity {
    @Id // Primary Key
    //GeneratedValue : 자동 번호 생성기(Oracle:Sequence), auto increment(Maria)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 100)
    private String productName;     // 상품명
    @Column(nullable = false)
    private Long price;             // 가격
    @Column(nullable = false)
    private int stock;              // 재고
    @Column(length = 50)
    private String category;        // 카테고리
}

package com.shinhan.bananaapp.manytoonereview.entity;

import com.shinhan.bananaapp.jpaprac.entity.entity2.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_order")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class OrderEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private int quantity;           // 주문 수량
    @Column(nullable = false)
    private Long totalPrice;        // 총 금액 (quantity × price)
    @Column(length = 20)
    @Builder.Default                //Builder 생성시 setting없으면 default로
    private String status = "ORDERED";  // ORDERED / CANCELLED
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime orderDate;
    // ── @ManyToOne — 핵심 ─────────────────────
    // 주문(N) → 상품(1)
    // LAZY: 주문 조회 시 상품은 실제 사용할 때만 조회
    @ManyToOne(fetch = FetchType.LAZY)
    //참조된 쪽에 컬럼이 생성된다. 컬럼의 이름은 field이름 + 상대 table의 키 field이름
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;
}

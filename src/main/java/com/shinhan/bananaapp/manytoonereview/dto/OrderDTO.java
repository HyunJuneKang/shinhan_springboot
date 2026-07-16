package com.shinhan.bananaapp.manytoonereview.dto;

import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDTO {
    private Long id;
    private int quantity;
    private Long totalPrice;
    private String status;
    private LocalDateTime orderDate;
    // 연관 상품 정보
    private Long productId;
    private String productName;
    private Long price;
}

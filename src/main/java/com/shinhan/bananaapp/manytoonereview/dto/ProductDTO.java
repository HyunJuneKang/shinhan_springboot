package com.shinhan.bananaapp.manytoonereview.dto;


import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {
    private Long id;
    private String productName;
    private Long price;
    private int stock;
    private String category;
    private LocalDateTime redDate;
    private LocalDateTime modDate;
}

package com.shinhan.bananaapp.manytoonereview.repository;


import com.shinhan.bananaapp.manytoonereview.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<ProductEntity,Long> {
    //카테고리로 조회하기
    List<ProductEntity> findByCategory(String category);
    //재고가 있는 상품 조회
    List<ProductEntity> findByStockGreaterThan(int stock);
    //상품명으로 조회
    List<ProductEntity> findByProductName(String productName);

}

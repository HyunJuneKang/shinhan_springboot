package com.shinhan.bananaapp.manytoonereview.repository;

import com.shinhan.bananaapp.manytoonereview.dto.projection.OrderCountDTO;
import com.shinhan.bananaapp.manytoonereview.entity.OrderEntity;
import com.shinhan.bananaapp.manytoonereview.entity.ProductEntity;
import com.shinhan.bananaapp.manytoonereview.repository.projection.OrderCountInterface;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<OrderEntity,Long> {
    //상품별 주문건수
    @Query(value = """
    SELECT p.product_name,COUNT(*)
        FROM tbl_order o JOIN tbl_product p ON o.product_id = p.id 
        GROUP BY p.product_name
    """,nativeQuery = true )
    List<OrderCountDTO> countAllByProductName();

    @Query("""
    select p.productName, count(o)
    from OrderEntity o
    join o.product p
    group by p.productName
    """)
    List<OrderCountInterface> countAllByProductName2();

    //특정상품의 주문 목록 조회
    @EntityGraph(attributePaths = "product")
    List<OrderEntity> findByProduct_ProductName(String productName);

    List<OrderEntity> findByProduct_PriceGreaterThan(Long productPriceIsGreaterThan);
    //주문상태로 조회
    List<OrderEntity> findByStatus(String status);

    List<OrderEntity> findByProduct(ProductEntity product);
    // 방법 2: 기존 findAll()에 EntityGraph 적용
    @Override
    @EntityGraph(attributePaths = "product")
    List<OrderEntity> findAll();

    // 방법 1: Fetch Join
    @Query("""
        select o
        from OrderEntity o
        join fetch o.product
        """)
    List<OrderEntity> findAll2();
}

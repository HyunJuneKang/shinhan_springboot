package com.shinhan.bananaapp;

import com.shinhan.bananaapp.manytoonereview.entity.OrderEntity;
import com.shinhan.bananaapp.manytoonereview.entity.ProductEntity;
import com.shinhan.bananaapp.manytoonereview.repository.OrderRepository;
import com.shinhan.bananaapp.manytoonereview.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
public class ManyToOneTest2 {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    OrderRepository orderRepository;

//    @Test
//    void f_lab8(){
//        System.out.println();
//        orderRepository.countAllByProductName().forEach(dto->{
//            System.out.println(dto.getCnt() + " " + dto.getProductName());
//        });
//
//        System.out.println();
//        orderRepository.countAllByProductName2().forEach(p->{
//            System.out.println(p.getCnt()+ " " + p.getProdName());
//        });
//
//    }

    @Test
    void f_lab4(){
        orderRepository.findByProduct_ProductName("그램")
                .forEach(System.out::println);
    }

    @Test
    void f_lab(){
        productRepository.findByCategory("노트북")
                .forEach(System.out::println);
    }

    @Test
    @Transactional
    void f_selectAllOrder() {
        List<OrderEntity> orders = orderRepository.findAll();

        System.out.println("===== 주문 조회 완료 =====");

        orders.forEach(order -> {
            System.out.println(
                    "orderId = " + order.getId()
                            + ", productName = "
                            + order.getProduct().getProductName()
            );
        });
    }

    @Test
    void f_selectAllProduct(){
        productRepository.findAll().forEach(System.out::println);
    }
    @Test
    @Transactional
    @Commit
    void f_insertOrder(){
        ProductEntity p1 = productRepository.getReferenceById(7L);
        OrderEntity order1 = OrderEntity.builder()
                .quantity(3)
                .totalPrice(1_000_000L)
                .product(p1)
                .build();
        ProductEntity p2 = productRepository.getReferenceById(8L);
        OrderEntity order2 = OrderEntity.builder()
                .quantity(7)
                .totalPrice(2_000_000L)
                .product(p2)
                .build();
        ProductEntity p3 = productRepository.getReferenceById(9L);
        OrderEntity order3 = OrderEntity.builder()
                .quantity(5)
                .totalPrice(5_000_000L)
                .product(p3)
                .build();
        orderRepository.save(order1);
        orderRepository.save(order2);
        orderRepository.save(order3);
    }

    @Test
    @Transactional
    @Commit
    public void f_insertProduct(){
        ProductEntity product1 = ProductEntity
                .builder()
                .productName("그램")
                .price(20_000_000L)
                .stock(100)
                .category("노트북")
                .build();
        ProductEntity product2 = ProductEntity
                .builder()
                .productName("그램2")
                .price(30_000_000L)
                .stock(120)
                .category("노트북")
                .build();
        ProductEntity product3 = ProductEntity
                .builder()
                .productName("그램3")
                .price(15_000_000L)
                .stock(130)
                .category("노트북")
                .build();
        productRepository.save(product1);
        productRepository.save(product2);
        productRepository.save(product3);
    }
}

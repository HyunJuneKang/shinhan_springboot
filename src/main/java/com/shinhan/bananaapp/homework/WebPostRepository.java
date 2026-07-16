package com.shinhan.bananaapp.homework;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WebPostRepository
        extends JpaRepository<WebPostEntity, Long> {

    // 제목/내용 검색 + 페이징
    @Query("SELECT p FROM WebPostEntity p " +
            "WHERE p.title LIKE %:keyword% " +
            "OR p.content LIKE %:keyword%")
    Page<WebPostEntity> searchByKeyword(
            @Param("keyword") String keyword, Pageable pageable);

    // 댓글 fetch join (N+1 방지)
    @Query("SELECT DISTINCT p FROM WebPostEntity p " +
            "LEFT JOIN FETCH p.comments " +
            "WHERE p.id = :id")
    WebPostEntity findByIdWithComments(@Param("id") Long id);
}
package com.shinhan.bananaapp.homework;


import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface WebCommentRepository
        extends JpaRepository<WebCommentEntity, Long> {

    List<WebCommentEntity> findByPostIdOrderByCreatedAtAsc(Long postId);
    int countByPostId(Long postId);
}
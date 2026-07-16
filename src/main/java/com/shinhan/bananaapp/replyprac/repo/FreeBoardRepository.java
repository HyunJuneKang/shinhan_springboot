package com.shinhan.bananaapp.replyprac.repo;

import com.shinhan.bananaapp.replyprac.entity.FreeBoardEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FreeBoardRepository extends JpaRepository<FreeBoardEntity,Long> {
    @EntityGraph(attributePaths = "replyList")
    public List<FreeBoardEntity>findAll();
    @Query("""
    select distinct b
    from FreeBoardEntity b
    join fetch b.replyList
    """)
    List<FreeBoardEntity> findAll2();
}

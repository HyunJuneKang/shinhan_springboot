package com.shinhan.bananaapp.replyprac.repository;

import com.shinhan.bananaapp.replyprac.entity.FreeBoardEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FreeBoardRepository extends JpaRepository<FreeBoardEntity,Long> {

    @EntityGraph(attributePaths = "replyList")
    public List<FreeBoardEntity> findAll();

    @Query("select b from FreeBoardEntity b left join fetch b.replyList order by b.bno desc")
    public List<FreeBoardEntity> findAll2();


}

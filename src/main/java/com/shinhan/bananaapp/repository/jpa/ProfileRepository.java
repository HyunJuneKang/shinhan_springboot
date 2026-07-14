package com.shinhan.bananaapp.repository.jpa;

import com.shinhan.bananaapp.entity3.ProfileEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProfileRepository extends JpaRepository<ProfileEntity,Long> {

    //1. 단순 조인
//    @Query("select p from ProfileEntity p join p.member")
    //2. Join Fetch
//    @Query("select p from ProfileEntity p join fetch p.member")
    //3. EntityGraph

    @EntityGraph(attributePaths = "member")
//    @Query("select p from ProfileEntity p")
    List<ProfileEntity> findAll();
}

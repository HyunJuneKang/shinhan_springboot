package com.shinhan.bananaapp.onetomany.reposity;

import com.shinhan.bananaapp.onetomany.entity.PDSBoardEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PDSBoardRepository extends JpaRepository<PDSBoardEntity,Long> {
    @EntityGraph(attributePaths = "files2")
    List<PDSBoardEntity> findAll();


}

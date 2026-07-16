package com.shinhan.bananaapp.onetomany.reposity;

import com.shinhan.bananaapp.onetomany.entity.PDSFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface PDSFileRepository extends JpaRepository<PDSFileEntity,Long> {

    @Transactional
    @Modifying
    @Query("""
    update PDSFileEntity f
    set f.pdsfilename =:fname
    where f.fno =:fno
    """)
    int updateFile(@Param("fno")Long fno,@Param("fname") String fname);
}

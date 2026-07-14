package com.shinhan.bananaapp.jpaprac.repository;

import com.shinhan.bananaapp.jpaprac.entity.entity1.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;

public interface BoardRepository extends JpaRepository<BoardEntity, Long> {

    //1.기본CRUD를 사용하기 위한 메서드 제공된다.
    //findAll(), findById(), save(), count(), deleteById()

    //2.규칙에 맞는 함수 정의
    //findByXXXXXXX
    //where writer = ?
    List<BoardEntity> findByWriter(String writer);
    //where title like concat('%', ?, '%')
    List<BoardEntity> findByTitleContaining(String t);
    List<BoardEntity> findByBnoBetween(Long bno1, Long bno2);
    //여러가지 조건으로 조회
    List<BoardEntity> findByBnoBetweenAndTitleContainingAndWriterBetweenOrderByBnoDesc(Long bnoAfter,
            Long bnoBefore, String title, String writer1, String writer2);
    // regDate가 ?이후 등록한 게시글
    List<BoardEntity> findByWriterAndBnoGreaterThanAndContentContainingAndTitleContainingAndRegDateAfter(String writer, Long bno, String content, String title, Timestamp regDateAfter);

    //3.JPQL(JPA Query Language)를 직접작성
    @Query( value = "select * from board where writer = ?1 ", nativeQuery = true)
    List<BoardEntity> f_findByWriter1(String writer);

    @Query("select b from BoardEntity b where b.writer = ?1")
    List<BoardEntity> f_findByWriter2(String writer);

    @Query("select b from BoardEntity b where b.writer = :wr ")
    List<BoardEntity> f_findByWriter3(@Param("wr") String writer);

    //배열사용, DTO사용, Interface사용

}

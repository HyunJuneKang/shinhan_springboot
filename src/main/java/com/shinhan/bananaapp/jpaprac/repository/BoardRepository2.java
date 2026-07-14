package com.shinhan.bananaapp.jpaprac.repository;

import com.shinhan.bananaapp.jpaprac.dto.Board2DTOMapping;
import com.shinhan.bananaapp.jpaprac.dto.BoardWriterCount;
import com.shinhan.bananaapp.jpaprac.entity.entity1.BoardEntity;
import com.shinhan.bananaapp.jpaprac.entity.entity1.BoardEntity2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface BoardRepository2
        extends JpaRepository<BoardEntity2, Long> {

    //1.기본CRUD를 사용하기 위한 메서드 제공된다.
    //findAll(), findById(), save(), count(), deleteById()

    //2.규칙에 맞는 함수 정의
    //findByXXXXXXX
    //where writer = ?
    List<BoardEntity2> findByWriter(String writer);
    //where title like concat('%', ?, '%')
    //List<BoardEntity> findByTitleContaining(String t);
    //List<BoardEntity> findByBnoBetween(Long bno1, Long bno2);
    //여러가지 조건으로 조회
    //List<BoardEntity> findByBnoBetweenAndTitleContainingAndWriterBetweenOrderByBnoDesc(Long bnoAfter,
    //        Long bnoBefore, String title, String writer1, String writer2);
    // regDate가 ?이후 등록한 게시글
    List<BoardEntity> findByWriterAndBnoGreaterThanAndContentContainingAndTitleContainingAndRegDateAfter(String writer, Long bno, String content, String title, LocalDate regDate);

    //3.JPQL(JPA Query Language)를 직접작성
    @Query( value = "select * from board2 where writer = ?1 ",
               nativeQuery = true)
    List<BoardEntity2> f_findByWriter1(String writer);

    //?1는 첫번째 파라메터라는 의미임
    @Query("select b from BoardEntity2 b where b.bno > ?2 and  b.writer = ?1")
    List<BoardEntity2> f_findByWriter2(String writer, Long bno);

    @Query("select b from BoardEntity2 b where b.bno > :no and  b.writer = :wr ")
    List<BoardEntity2> f_findByWriter3(@Param("wr") String writer, @Param("no") Long bno);

    //작성자별 board의 개수
    @Query("select b.writer writer,COUNT(b.bno) cnt from BoardEntity2 b group by b.writer")
    List<Object[]> f_countByWriter();

    //배열사용, DTO사용, Interface사용
    @Query("""
           SELECT b.writer, COUNT(b.bno)
           FROM BoardEntity2 b
           GROUP BY b.writer
           """)
    List<Object[]> f_countByWriterUsingList();

    @Query("""
           SELECT new com.shinhan.bananaapp.jpaprac.dto.Board2DTO(
               b.writer,
               COUNT(b.bno)
           )
           FROM BoardEntity2 b
           GROUP BY b.writer
           ORDER BY b.writer
           """)
    List<Board2DTOMapping> f_countByWriterUsingConstructor();

    //3)Interface 사용
    @Query("""
           SELECT b.writer AS writer,
                  COUNT(b.bno) AS cnt
           FROM BoardEntity2 b
           GROUP BY b.writer
           ORDER BY b.writer
           """)
    List<BoardWriterCount> f_countByWriterUsingInterface();
}

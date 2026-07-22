package com.shinhan.bananaapp.board;

import com.shinhan.bananaapp.jpaprac.entity.entity1.BoardEntity;
import com.shinhan.bananaapp.jpaprac.repository.BoardRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.IntStream;

@Slf4j
@SpringBootTest
//@RequiredArgsConstructor
public class BoardTest {
//    private final BoardRepository boardRepository;
    @Autowired
    BoardRepository boardRepository;
    @Test
    public void f_insert(){
        IntStream.rangeClosed(0,10).forEach(i->{
            BoardEntity board = BoardEntity
                    .builder()
                    .title("밥" + i)
                    .writer("배고파" + i)
                    .build();
            boardRepository.save(board);
        });
    }
    @Test
    public void f_selectAll(){
        List<BoardEntity> boardList = boardRepository.findAll();
        boardList.forEach(System.out::println);
    }
    @Test
    public void f_2(){
        boardRepository.findByWriter("배고파1").forEach(System.out::println);
        boardRepository.findByTitleContaining("밥").forEach(System.out::println);
    }
    @Test
    public void f_3(){
        boardRepository.findByBnoBetween(1L,3L).forEach(System.out::println);
    }
}
//

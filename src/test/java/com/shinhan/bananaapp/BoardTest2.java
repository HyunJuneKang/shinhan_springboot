package com.shinhan.bananaapp;

import com.shinhan.bananaapp.jpaprac.dto.Board2DTOMapping;
import com.shinhan.bananaapp.jpaprac.entity.entity1.BoardEntity2;
import com.shinhan.bananaapp.jpaprac.repository.BoardRepository2;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;

import java.util.List;
import java.util.stream.IntStream;

@SpringBootTest
public class BoardTest2 {

    @Autowired
    BoardRepository2 repo;
    @Autowired
    ModelMapper modelMapper;

    @Test
    void f_entityToDTO() {
        BoardEntity2 entity = repo.findById(1L)
                .orElseThrow(() ->
                        new IllegalArgumentException("게시글이 존재하지 않습니다.")
                );

        Board2DTOMapping dto = Board2DTOMapping.builder()
                .bno(entity.getBno())
                .writer(entity.getWriter())
                .content(entity.getContent())
                .title(entity.getTitle())
                .build();

        System.out.println(entity);
        System.out.println(dto);

        //ModelMapper 이용하기
        Board2DTOMapping dto2 = modelMapper.map(entity,Board2DTOMapping.class);
        System.out.println("Entity->DTO : " + dto2);
        BoardEntity2 entity2 = modelMapper.map(dto2,BoardEntity2.class);
        System.out.println("DTO->Entity : " + dto2);
    }

    @Test
    void f_paging1(){
        Pageable pageable1 = PageRequest.of(0,3);
        Pageable pageable2 = PageRequest.of(0,3, Sort.by("bno"));
        Pageable pageable3 = PageRequest.of(0,3,Sort.Direction.DESC,new String[]{"bno"});
        repo.findAll(pageable1).forEach(System.out::println);

        Page<BoardEntity2> result = repo.findAll(pageable2);
        System.out.println("전체 페이지 수: " + result.getTotalPages());
        System.out.println("전체 건 수: " + result.getTotalElements());
        System.out.println("현재 페이지 수: " + result.getNumber());
        System.out.println("한 페이지의 건 수: " + result.getSize());
        List<BoardEntity2> blist = result.getContent();
        blist.forEach(System.out::println);
    }
    @Test
    void f_list(){
        repo.f_countByWriterUsingList().forEach(arr->{
            System.out.println(arr[0]+"   "+arr[1]);
        });
    }
    @Test
    void f_dto(){
        repo.f_countByWriterUsingConstructor().forEach(System.out::println);
    }
    @Test
    void f_interface(){
        repo.f_countByWriterUsingInterface().forEach(inter->{
            System.out.println(inter.getCnt() + " " + inter.getWriter());
        });
    }
    @Test
    void f_jpql(){
        repo.f_countByWriter().forEach(arr->{
            System.out.println(arr[0] + " 작성자가 쓴 글은 " + arr[1] + "건입니다.");
        });
    }
    @Test
    void f_makeFunction2(){
        System.out.println("==========f_findByWriter2================");
        repo.f_findByWriter2("user-3", 10L).forEach(System.out::println);
        System.out.println("==========f_findByWriter3================");
        repo.f_findByWriter3("user-3", 10L).forEach(System.out::println);

    }
    @Test
    void f_makeFunction(){
        repo.findByWriter("user-3").forEach(System.out::println);
    }

    //모두조회, 키로조회, 수정, 삭제 , 건수
    @Test
    void f4(){
        repo.deleteById(2L);
        System.out.println(repo.count() + "건");
    }
    @Commit
    @Transactional
    @Test
    void f3(){
        repo.findById(1L).ifPresentOrElse(board->{
            board.setContent("=====오늘은 비소식있어요======");
            board.setWriter("jin");  //repo.save(board)
        }, ()->{ System.out.println("존재하지않는 Board입니다."); });
    }
    @Test
    void f2(){
        System.out.println("------------모두조회------------");
        repo.findAll().forEach(System.out::println);
        System.out.println("------------키로 조회------------");
        repo.findById(1L).ifPresentOrElse(board->{
            System.out.println(board);
        }, ()->{ System.out.println("존재하지않는 Board입니다."); });
    }
    @Test
    public void f1(){
        IntStream.rangeClosed(1,10).forEach(i->{
            BoardEntity2 board = BoardEntity2.builder()
                    .title("화요일-" + i)
                    .content("JPA 학습 - " + (i%5))
                    .writer("user-" + (i%3+1))
                    .build();
            repo.save(board);
        });
    }
}

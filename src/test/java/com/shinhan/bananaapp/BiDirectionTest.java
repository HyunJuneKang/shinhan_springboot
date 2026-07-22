package com.shinhan.bananaapp;

import com.shinhan.bananaapp.replyprac.entity.FreeBoardEntity;
import com.shinhan.bananaapp.replyprac.entity.FreeReplyEntity;
import com.shinhan.bananaapp.replyprac.repository.FreeBoardRepository;
import com.shinhan.bananaapp.replyprac.repository.FreeReplyRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@SpringBootTest
public class BiDirectionTest {
    @Autowired
    FreeBoardRepository freeBoardRepository;
    @Autowired
    FreeReplyRepository freeReplyRepository;
    @Transactional
    @Test
    void f_selectAll(){
        List<FreeReplyEntity> freeReplyEntities = freeReplyRepository.findAll();
        freeReplyEntities.forEach(reply->{
            System.out.println(reply.getBoard());
        });
        System.out.println("================================================");
        List<FreeBoardEntity> freeBoardEntities = freeBoardRepository.findAll();
        freeBoardEntities.forEach(board->{
            board.getReplyList().forEach(System.out::println);
        });
    }


    @Transactional
    @Commit
    @Test
    void updateBoard(){
        Long bno = 3L;
        FreeBoardEntity freeBoard = freeBoardRepository.getReferenceById(bno);
        List<FreeReplyEntity> replyList = new ArrayList<>();
        IntStream.rangeClosed(1,3).forEach(j->{
            FreeReplyEntity reply = FreeReplyEntity.builder()
                    .reply("집중"+":"+j)
                    .replyer("user"+j)
                    .board(freeBoard)
                    .build();
            replyList.add(reply);
        });
        freeBoard.setReplyList(replyList);
    }

    @Test
    void boardInsert2(){
        IntStream.rangeClosed(100,105).forEach(i->{
            FreeBoardEntity freeBoard = FreeBoardEntity
                    .builder()
                    .title("freeboard" + i)
                    .content("졸지말기" + i)
                    .writer("작성자" + i)
                    .build();
            List<FreeReplyEntity> replyList = new ArrayList<>();
            IntStream.rangeClosed(1,3).forEach(j->{
                FreeReplyEntity reply = FreeReplyEntity.builder()
                        .reply("집중"+i+":"+j)
                        .replyer("user"+j)
                        .board(freeBoard)
                        .build();
                replyList.add(reply);
            });
            freeBoard.setReplyList(replyList);
            freeBoardRepository.save(freeBoard);
        });
    }

    //Board insert
    @Test
    void boardInsert(){
        IntStream.rangeClosed(0,10).forEach(i->{
            FreeBoardEntity freeBoard = FreeBoardEntity
                    .builder()
                    .title("ㅋ" + i)
                    .content("ㄴ" + i)
                    .writer("가" + i)
                    .build();
            freeBoardRepository.save(freeBoard);
        });
    }
}

package com.shinhan.bananaapp;

import com.shinhan.bananaapp.onetomany.reposity.PDSBoardRepository;
import com.shinhan.bananaapp.onetomany.reposity.PDSFileRepository;
import com.shinhan.bananaapp.onetomany.entity.PDSBoardEntity;
import com.shinhan.bananaapp.onetomany.entity.PDSFileEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
public class OneToManyTest {
    @Autowired
    PDSBoardRepository pdsBoardRepository;
    @Autowired
    PDSFileRepository pdsFileRepository;
    @Autowired
    private PDSFileRepository pDSFileRepository;

    @Transactional
    @Commit
    @Test
    void updateFile(){
        pdsFileRepository.findById(5L).ifPresent(f->{
            pDSFileRepository.save(f);
        });
    }
    @Test
    void f_deleteFile(){
        pdsFileRepository.deleteById(2L);
    }

    @Test
    @Transactional
    void selectAll(){
        pdsBoardRepository.findAll().forEach(board->{
            System.out.println(board+ "-->" + board.getFiles2().size());
        });
    }


    @Test
    @Commit
    @Transactional
    void update(){
        Long bno = 1L;
        pdsBoardRepository.findById(bno).ifPresent(board->{
            board.setPname("삼계탕 먹는날");
            board.setPwriter("박채빈");
            List<PDSFileEntity> pileList = board.getFiles2();
            pileList.remove(0);
            pileList.add(PDSFileEntity
                    .builder()
                    .pdsfilename("zz2.png")
                    .build());
        });
    }

    //1(board)----> N(file)
    @Test
    void insert(){
        //cascade 영속성전이
        //insert PDSBoard , insert PDFFile, update PDSFile
        List<PDSFileEntity> filesList = List.of(
                PDSFileEntity.builder()
                        .pdsfilename("ab.png")
                        .build(),
                PDSFileEntity.builder()
                        .pdsfilename("bc.png")
                        .build(),
                PDSFileEntity.builder()
                        .pdsfilename("c.png")
                        .build()
        );
        PDSBoardEntity board = PDSBoardEntity
                .builder()
                .pname("오늘은 복날")
                .pwriter("채빈")
//                .files2(filesList)
                .build();
        pdsBoardRepository.save(board);
    }

}

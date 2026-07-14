package com.shinhan.bananaapp;


import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shinhan.bananaapp.entity2.QWebBoardEntity;
import com.shinhan.bananaapp.entity2.WebBoardEntity;
import com.shinhan.bananaapp.repository.jpa.WebBoardRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.IntStream;

@SpringBootTest
public class WebBoardTest {
    @Autowired
    WebBoardRepository repo;

    @Autowired
    JPAQueryFactory queryFactory;

    @Test
    void f3(){
        //select * from where bno > 5 order bu bno
        QWebBoardEntity board = QWebBoardEntity.webBoardEntity;
        List<WebBoardEntity> blist = queryFactory.selectFrom(board)
                .where(board.bno.gt(5L),
                        (board.writer.eq("user7"))
                                .or(board.title.eq("ㅋㅋ"))
                )
                .orderBy(board.bno.desc())
                .fetch();
        blist.forEach(System.out::println);
    }
    @Test
    void f1(){
        //content, title, writer
        Predicate predicate = repo.makePredicate("ctw","JPA");
        repo.findAll(predicate).forEach(System.out::println);
    }

    @Test
    void f2() {
        IntStream.rangeClosed(1, 10).forEach(i -> {
            repo.save(WebBoardEntity
                    .builder()
                    .title("ㅋㅋ" + i)
                    .writer("user" + i)
                    .content("ㅇㅅㅇ" + i)
                    .build()
            );
        });
        IntStream.rangeClosed(1, 10).forEach(i -> {
            repo.save(WebBoardEntity
                    .builder()
                    .title("JPA-aa")
                    .writer("user" + i)
                    .content("ㅇㅅㅇ" + i)
                    .build()
            );
        });
        IntStream.rangeClosed(1, 10).forEach(i -> {
            repo.save(WebBoardEntity
                    .builder()
                    .title("ㅋㅋ")
                    .writer("JPA-user" + i)
                    .content("ㅇㅅㅇ" + i)
                    .build()
            );
        });
        IntStream.rangeClosed(1, 10).forEach(i -> {
            repo.save(WebBoardEntity
                    .builder()
                    .title("ㅋㅋ")
                    .writer("user" + i)
                    .content("JPA" + i)
                    .build()
            );
        });
    }

}

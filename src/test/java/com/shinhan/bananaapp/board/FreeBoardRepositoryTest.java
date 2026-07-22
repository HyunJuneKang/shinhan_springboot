package com.shinhan.bananaapp.board;

import com.shinhan.bananaapp.replyprac.entity.FreeBoardEntity;
import com.shinhan.bananaapp.replyprac.repository.FreeBoardRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(properties = {
        "spring.sql.init.mode=never"
})
@DisplayName("FreeBoardRepository 슬라이스 테스트")

class FreeBoardRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private FreeBoardRepository boardRepository;

    @Test
    @DisplayName("게시글 저장 후 조회")
    void saveAndFind() {
        // given
        FreeBoardEntity board = FreeBoardEntity.builder()
                .title("스프링 강의")
                .content("내용")
                .writer("홍길동")
                .build();
        em.persist(board);
        em.flush();   // 영속성 컨텍스트 → DB INSERT
        em.clear();   // 1차 캐시 초기화 → 실제 SELECT 발생

        // when
        FreeBoardEntity found =
                boardRepository.findById(board.getBno())
                        .orElseThrow();

        // then
        assertThat(found.getTitle()).isEqualTo("스프링 강의");
        assertThat(found.getWriter()).isEqualTo("홍길동");
    }

    @Test
    @DisplayName("전체 목록 조회 — 2건")
    void findAll_2건() {
        // given
        em.persist(FreeBoardEntity.builder()
                .title("스프링").content("내용1")
                .writer("홍길동").build());
        em.persist(FreeBoardEntity.builder()
                .title("JPA").content("내용2")
                .writer("김철수").build());
        em.flush(); em.clear();

        // when
        List<FreeBoardEntity> result =
                boardRepository.findAll();

        // then
        assertThat(result).hasSize(2)
                .extracting("writer")
                .containsExactlyInAnyOrder("홍길동", "김철수");
    }

    @Test
    @DisplayName("findAll2 — fetch join 댓글 포함 조회")
    void findAll2_fetchJoin() {
        // given
        FreeBoardEntity board = FreeBoardEntity.builder()
                .title("댓글 테스트").content("내용")
                .writer("홍길동").build();
        em.persist(board);
        em.flush(); em.clear();

        // when — fetch join 쿼리
        List<FreeBoardEntity> result =
                boardRepository.findAll2();

        // then
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getReplyList())
                .isNotNull(); // LazyInitializationException 없음
    }
}

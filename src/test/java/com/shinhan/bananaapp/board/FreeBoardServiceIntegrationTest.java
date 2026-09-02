package com.shinhan.bananaapp.board;

import com.shinhan.bananaapp.replyprac.dto.FreeBoardDTO;
import com.shinhan.bananaapp.replyprac.repository.FreeBoardRepository;
import com.shinhan.bananaapp.replyprac.service.FreeBoardService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional   // 테스트 후 DB 자동 롤백
@DisplayName("FreeBoardService 통합 테스트")
class FreeBoardServiceIntegrationTest {

    @Autowired
    private FreeBoardService boardService;

    @Autowired
    private FreeBoardRepository boardRepository;

    // @MockitoBean 없음 — 실제 Service, Repository, DB 사용

    @Test
    @DisplayName("register → f_detail 전체 흐름")
    void register_and_detail() {
        // given
        FreeBoardDTO inputDto = FreeBoardDTO.builder()
                .title("통합 테스트 게시글")
                .content("통합 테스트 내용")
                .writer("홍길동")
                .build();

        // when — 실제 DB INSERT
        FreeBoardDTO saved = boardService.register(inputDto);

        // then — 등록 확인
        assertThat(saved).isNotNull();
        assertThat(saved.getBno()).isNotNull();
        assertThat(saved.getTitle())
                .isEqualTo("통합 테스트 게시글");

        // when — 실제 DB SELECT
        FreeBoardDTO found = boardService.f_detail(saved.getBno());

        // then — 조회 확인
        assertThat(found.getWriter()).isEqualTo("홍길동");
        assertThat(found.getReplyList()).isEmpty();
    }

    @Test
    @DisplayName("register → modify → f_detail 수정 흐름")
    void register_and_modify() {
        // ── 1. 등록 ─────────────────────────────
        FreeBoardDTO saved = boardService.register(
                FreeBoardDTO.builder()
                        .title("원본 제목").content("원본 내용")
                        .writer("홍길동").build());

        // ── 2. 수정 ─────────────────────────────
        boardService.modify(FreeBoardDTO.builder()
                .bno(saved.getBno())
                .title("수정된 제목")
                .content("수정된 내용")
                .build());

        // ── 3. 수정 확인 ─────────────────────────
        FreeBoardDTO updated =
                boardService.f_detail(saved.getBno());
        assertThat(updated.getTitle())
                .isEqualTo("수정된 제목");
        assertThat(updated.getContent())
                .isEqualTo("수정된 내용");
    }

    @Test
    @DisplayName("register → remove → f_detail 삭제 흐름")
    void register_and_remove() {
        // ── 1. 등록 ─────────────────────────────
        FreeBoardDTO saved = boardService.register(
                FreeBoardDTO.builder()
                        .title("삭제 테스트").content("내용")
                        .writer("홍길동").build());
        Long bno = saved.getBno();
        assertThat(bno).isNotNull();

        // ── 2. 삭제 ─────────────────────────────
        boardService.remove(bno);

        // ── 3. 삭제 확인 ─────────────────────────
        // 삭제 후 f_detail 호출 → null or 예외
        FreeBoardDTO deleted = boardService.f_detail(bno);
        assertThat(deleted).isNull();
        // 또는
        // assertThrows(NoSuchElementException.class,
        //     () -> boardService.f_detail(bno));
    }

    @Test
    @DisplayName("getList → 2건 등록 후 목록 크기 확인")
    void getList_2건() {
        // given
        boardService.register(FreeBoardDTO.builder()
                .title("글1").content("내용1").writer("홍길동").build());
        boardService.register(FreeBoardDTO.builder()
                .title("글2").content("내용2").writer("김철수").build());

        // when
        List<FreeBoardDTO> list = boardService.getList();

        // then
        assertThat(list).hasSizeGreaterThanOrEqualTo(2);
        assertThat(list)
                .extracting("writer")
                .contains("홍길동", "김철수");
    }
}

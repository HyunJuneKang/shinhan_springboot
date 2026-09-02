package com.shinhan.bananaapp.board;

import com.shinhan.bananaapp.replyprac.dto.FreeBoardDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment =
        SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("FreeBoard HTTP 통합 테스트")
class FreeBoardHttpIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("POST /freeboard/register → 201 + DTO 반환")
    void register_http() {
        // given
        FreeBoardDTO dto = FreeBoardDTO.builder()
                .title("HTTP 테스트")
                .content("내용")
                .writer("홍길동")
                .build();

        // when — 실제 HTTP POST
        ResponseEntity<FreeBoardDTO> res =
                restTemplate.postForEntity(
                        "/freeboard/register",
                        dto, FreeBoardDTO.class);

        // then
        assertThat(res.getStatusCode())
                .isEqualTo(HttpStatus.CREATED);
        assertThat(res.getBody().getBno()).isNotNull();
        assertThat(res.getBody().getTitle())
                .isEqualTo("HTTP 테스트");
    }

    @Test
    @DisplayName("CRUD 전체 HTTP 흐름")
    void crud_http_flow() {
        // ── 1. 등록 ─────────────────────────────
        FreeBoardDTO newBoard = FreeBoardDTO.builder()
                .title("원본").content("내용").writer("홍길동")
                .build();
        ResponseEntity<FreeBoardDTO> createRes =
                restTemplate.postForEntity(
                        "/freeboard/register", newBoard, FreeBoardDTO.class);
        assertThat(createRes.getStatusCode())
                .isEqualTo(HttpStatus.CREATED);
        Long bno = createRes.getBody().getBno();

        // ── 2. 수정 ─────────────────────────────
        FreeBoardDTO updateDto = FreeBoardDTO.builder()
                .bno(bno).title("수정됨").content("수정 내용")
                .build();
        restTemplate.put("/freeboard/modify", updateDto);

        // ── 3. 수정 확인 ─────────────────────────
        ResponseEntity<FreeBoardDTO> getRes =
                restTemplate.getForEntity(
                        "/freeboard/detail?bno=" + bno,
                        FreeBoardDTO.class);
        assertThat(getRes.getBody().getTitle())
                .isEqualTo("수정됨");

        // ── 4. 삭제 ─────────────────────────────
        restTemplate.delete("/freeboard/remove?bno=" + bno);

        // ── 5. 삭제 확인 ─────────────────────────
        ResponseEntity<FreeBoardDTO> deletedRes =
                restTemplate.getForEntity(
                        "/freeboard/detail?bno=" + bno,
                        FreeBoardDTO.class);
        assertThat(deletedRes.getStatusCode())
                .isEqualTo(HttpStatus.NOT_FOUND);
    }
}

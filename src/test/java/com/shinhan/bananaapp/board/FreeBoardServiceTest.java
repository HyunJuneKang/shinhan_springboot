package com.shinhan.bananaapp.board;

import com.shinhan.bananaapp.replyprac.dto.FreeBoardDTO;
import com.shinhan.bananaapp.replyprac.dto.FreeReplyDTO;
import com.shinhan.bananaapp.replyprac.entity.FreeBoardEntity;
import com.shinhan.bananaapp.replyprac.entity.FreeReplyEntity;
import com.shinhan.bananaapp.replyprac.repository.FreeBoardRepository;
import com.shinhan.bananaapp.replyprac.service.FreeBoardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;


//JUnit5에게 "이 클래스에서 Mockito 기능을 사용하겠다고 알려주는 확장 기능
@ExtendWith(MockitoExtension.class)
// 리니언트 : 관대한, 느슨함
// 스트릭트니스 기본값이 STRICT_STUBS, given/when  스텁을 만들어넣고 사용하지않으면 오류
// 엄격한 검사를 꺼서, 스텁을 설정해놓고 안 써도 에러가 안 나게 만듦
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("WebBoardService 단위 테스트")
class FreeBoardServiceTest {
    //가짜, DB 없이, Service의 로직 흐름만 검증
    @Mock
    FreeBoardRepository boardRepo;
    @Mock
    ModelMapper modelMapper;
    @InjectMocks //서비스는 진짜, 위의 2개의 목은 가짜.@Mock인거만 주입됨
    FreeBoardService boardService;

    // ── 공통 픽스처(Fixture) ─────────────────────
    FreeBoardEntity entity1, entity2;
    FreeBoardDTO dto1, dto2;

    //각 테스트 메서드가 실행되기 "직전"마다 매번 새로 호출
    @BeforeEach
    void setUp() {
        // Entity 픽스처
        entity1 = FreeBoardEntity.builder()
                .bno(1L).title("스프링 강의").content("내용1")
                .writer("홍길동").replyList(new ArrayList<>()).build();

        entity2 = FreeBoardEntity.builder()
                .bno(2L).title("JPA 강의").content("내용2")
                .writer("김철수").replyList(new ArrayList<>()).build();

        // DTO 픽스처
        dto1 = FreeBoardDTO.builder()
                .bno(1L).title("스프링 강의").writer("홍길동")
                .replyList(new ArrayList<>()).replyCount(0L).build();

        dto2 = FreeBoardDTO.builder()
                .bno(2L).title("JPA 강의").writer("김철수")
                .replyList(new ArrayList<>()).replyCount(0L).build();
    }

    //Nested : test별로 grouping하기위해 묶음

    @Test
    @DisplayName("존재하는 bno 조회 → DTO 반환 및 replyCount 세팅")
    void f_detail_존재하는bno_DTO반환() {

        // ── given ──────────────────────────────
        // 댓글 2개 포함된 entity
        FreeReplyEntity reply1 = FreeReplyEntity.builder().rno(1L).build();
        FreeReplyEntity reply2 = FreeReplyEntity.builder().rno(2L).build();
        entity1.setReplyList(List.of(reply1, reply2));

        FreeBoardDTO dtoWithReplies = FreeBoardDTO.builder()
                .bno(1L).title("스프링 강의").writer("홍길동")
                .replyList(List.of(
                        FreeReplyDTO.builder().rno(1L).build(),
                        FreeReplyDTO.builder().rno(2L).build()
                )).build();

        given(boardRepo.findById(1L)).willReturn(Optional.of(entity1));
        given(modelMapper.map(entity1, FreeBoardDTO.class))
                .willReturn(dtoWithReplies);

        // ── when ───────────────────────────────
        FreeBoardDTO result = boardService.f_detail(1L);

        // ── then ───────────────────────────────
        assertThat(result).isNotNull();
        assertThat(result.getBno()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("스프링 강의");
        assertThat(result.getReplyCount()).isEqualTo(2L);
        assertThat(result.getReplyList()).hasSize(2);
        // reply.bno 가 게시글 bno 로 세팅되는지 확인
        assertThat(result.getReplyList())
                .extracting("bno").containsOnly(1L);
    }

    @Test
    @DisplayName("존재하지 않는 bno 조회 → null 반환")
    void f_detail_없는bno_null반환() {

        // ── given ──────────────────────────────
        given(boardRepo.findById(999L)).willReturn(Optional.empty());

        // ── when ───────────────────────────────
        FreeBoardDTO result = boardService.f_detail(999L);

        // ── then ───────────────────────────────
        assertThat(result).isNull();
        // modelMapper는 호출되면 안 됨
        verify(modelMapper, never()).map(any(), any());
    }

    @Test
    @DisplayName("댓글 없는 게시글 → replyCount = 0")
    void f_detail_댓글없음_replyCount0() {

        // ── given ──────────────────────────────
        given(boardRepo.findById(1L)).willReturn(Optional.of(entity1));
        given(modelMapper.map(entity1, FreeBoardDTO.class)).willReturn(dto1);
        // dto1.replyList = new ArrayList() → size = 0

        // ── when ───────────────────────────────
        FreeBoardDTO result = boardService.f_detail(1L);

        // ── then ───────────────────────────────
        assertThat(result.getReplyCount()).isZero();
        assertThat(result.getReplyList()).isEmpty();
    }
    // ── getList() 는 boardRepository.findAll2() (JPQL join fetch)를 사용하므로
    // 스터빙(stubbing) 대상도 findAll2() 여야 한다. boardService(@InjectMocks) 자체를
    // given() 금지 — 테스트 대상을 가짜로 만들어버리는 것이라 아무것도 검증하지 못하게 된다.
    //스터빙(stubbing) : 테스트 대상 로직의 독립적인 검증을 위해 가짜 응답을 반환하도록 설정하는 외부 의존성(객체,메서드,API)의미
    //******즉,  given()/willReturn()은 Mockito가 @Mock으로 만든 가짜 객체에만 걸 수 있는 기능

    @Test
    @DisplayName("게시글 2건 → DTO 목록 2건 반환")
    void getList_2건반환() {

        // ── given ──────────────────────────────
        given(boardRepo.findAll2())
                .willReturn(List.of(entity2, entity1));
        given(modelMapper.map(entity2, FreeBoardDTO.class)).willReturn(dto2);
        given(modelMapper.map(entity1, FreeBoardDTO.class)).willReturn(dto1);

        // ── when ───────────────────────────────
        List<FreeBoardDTO> result = boardService.getList();

        // ── then ───────────────────────────────
        assertThat(result).hasSize(2);
        assertThat(result).extracting("bno").containsExactly(2L, 1L);
        assertThat(result).extracting("replyCount").containsExactly(0L, 0L);
    }

    @Test
    @DisplayName("게시글 없음 → 빈 리스트 반환")
    void getList_데이터없음_빈리스트() {

        // ── given ──────────────────────────────
        given(boardRepo.findAll2()).willReturn(Collections.emptyList());

        // ── when ───────────────────────────────
        List<FreeBoardDTO> result = boardService.getList();

        // ── then ───────────────────────────────
        assertThat(result).isEmpty();
        // modelMapper 호출 안 됨 확인
        verify(modelMapper, never()).map(any(), any());
    }

    @Test
    @DisplayName("정상 등록 → 저장된 bno로 DTO 반환")
    void register_정상등록_DTO반환() {

        // ── given ──────────────────────────────
        // 등록 요청으로 들어온 DTO (아직 bno 없음)
        FreeBoardDTO inputDto = FreeBoardDTO.builder()
                .title("Spring 강의").content("내용").writer("홍길동")
                .build();

        // ① DTO → Entity 변환 결과 (저장 전이라 bno 없음)
        FreeBoardEntity mappedEntity = FreeBoardEntity.builder()
                .title("Spring 강의").content("내용").writer("홍길동")
                .replyList(new ArrayList<>()).build();

        // ② Repository.save() 후 (IDENTITY 전략으로 bno가 채워진 상태)
        FreeBoardEntity savedEntity = FreeBoardEntity.builder()
                .bno(3L).title("Spring 강의").content("내용").writer("홍길동")
                .replyList(new ArrayList<>()).build();

        // ③ Entity → DTO 변환 결과 (최종적으로 호출자에게 돌아갈 값)
        FreeBoardDTO savedDto = FreeBoardDTO.builder()
                .bno(3L).title("Spring 강의").writer("홍길동")
                .replyList(new ArrayList<>()).build();

        given(modelMapper.map(inputDto, FreeBoardEntity.class)).willReturn(mappedEntity);
        given(boardRepo.save(mappedEntity)).willReturn(savedEntity);
        given(modelMapper.map(savedEntity, FreeBoardDTO.class)).willReturn(savedDto);

        // ── when ───────────────────────────────
        FreeBoardDTO result = boardService.register(inputDto);

        // ── then ───────────────────────────────
        assertThat(result).isNotNull();
        assertThat(result.getBno()).isEqualTo(3L);
        assertThat(result.getTitle()).isEqualTo("Spring 강의");
        assertThat(result.getReplyCount()).isZero(); // 신규 등록 → 댓글 없음
    }

    @Test
    @DisplayName("등록 시 Repository.save()가 정확히 1번, 변환된 Entity로 호출된다")
    void register_save_1번호출검증 () {

        // ── given ──────────────────────────────
        FreeBoardDTO inputDto = FreeBoardDTO.builder()
                .title("JPA 강의").content("내용").writer("김철수")
                .build();

        FreeBoardEntity mappedEntity = FreeBoardEntity.builder()
                .title("JPA 강의").content("내용").writer("김철수")
                .replyList(new ArrayList<>()).build();

        FreeBoardEntity savedEntity = FreeBoardEntity.builder()
                .bno(4L).title("JPA 강의").content("내용").writer("김철수")
                .replyList(new ArrayList<>()).build();

        FreeBoardDTO savedDto = FreeBoardDTO.builder()
                .bno(4L).title("JPA 강의").writer("김철수")
                .replyList(new ArrayList<>()).build();

        given(modelMapper.map(inputDto, FreeBoardEntity.class)).willReturn(mappedEntity);
        given(boardRepo.save(mappedEntity)).willReturn(savedEntity);
        given(modelMapper.map(savedEntity, FreeBoardDTO.class)).willReturn(savedDto);

        // ── when ───────────────────────────────
        boardService.register(inputDto);

        // ── then ───────────────────────────────
        // Service가 Repository에 "DTO가 아니라 변환된 Entity"를 정확히 1번 넘겼는지 검증
        verify(boardRepo, times(1)).save(mappedEntity);
    }

    @Test
    @DisplayName("게시글 수정 → save 1회 호출, 수정된 bno 반환")
    void updateBoard_수정성공_bno반환() {

        // ── given ──────────────────────────────
        FreeBoardDTO updateDTO = FreeBoardDTO.builder()
                .bno(1L)
                .title("수정된 제목")
                .content("수정된 내용")
                .build();

        // ✅ findBoard() → findById() 호출에 대한 Mock 설정
        given(boardRepo.findById(1L))
                .willReturn(Optional.of(entity1));

        // ── when ───────────────────────────────
        boardService.modify(updateDTO);

        // ── then ───────────────────────────────
        assertThat(entity1.getTitle())
                .isEqualTo("수정된 제목");
        assertThat(entity1.getContent())
                .isEqualTo("수정된 내용");
        // findById 1회 호출됐는지 확인
        verify(boardRepo, times(1)).findById(1L);
        // save는 호출 안 됨 확인
        verify(boardRepo, never()).save(any());
    }


    @Test
    @DisplayName("존재하는 bno 삭제 → deleteById 1회 호출")
    void remove_삭제성공() {

        // ── given ──────────────────────────────
        // cascade = ALL → deleteById 호출 시
        // 연관된 FreeReply 자동 삭제
        given(boardRepo.findById(1L))
                .willReturn(Optional.of(entity1));
        willDoNothing().given(boardRepo)
                .deleteById(1L);

        // ── when ───────────────────────────────
        boardService.remove(1L);

        // ── then ───────────────────────────────
        verify(boardRepo, times(1)).deleteById(1L);
        // modify가 호출되면 안 됨
        verify(boardRepo, never()).save(any());
    }

    @Test
    @DisplayName("존재하지 않는 bno 삭제 → 예외 발생")
    void remove_없는bno_예외() {

        // ── given ──────────────────────────────
        given(boardRepo.findById(999L))
                .willReturn(Optional.empty());

        // ── when & then ────────────────────────
        assertThrows(NoSuchElementException.class,
                () -> boardService.remove(999L));

        verify(boardRepo, never()).deleteById(any());
    }
}
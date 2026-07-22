package com.shinhan.bananaapp.board;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shinhan.bananaapp.replyprac.controller.FreeBoardController;
import com.shinhan.bananaapp.replyprac.dto.FreeBoardDTO;
import com.shinhan.bananaapp.replyprac.service.FreeBoardService;
import com.shinhan.bananaapp.security.MemberService;
import com.shinhan.bananaapp.security.jwt.JwtAuthFilter;
import com.shinhan.bananaapp.security.jwt.JwtUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// MockMvc 요청 메서드
// get(), post(), put(), delete() 포함
// MockMvc 결과 검증
// status(), jsonPath(), content() 포함
// 콘솔 출력
// print() 포함
// MediaType
// MockMvc + ObjectMapper
// @WebMvcTest + @MockitoBean
// 또는 이전 버전
// import org.springframework.boot.test.mock.mockito.MockBean;

// Mockito given
// given(), willReturn(), willDoNothing(), any() 포함


@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = FreeBoardController.class, excludeAutoConfiguration = {
        SecurityAutoConfiguration.class,
        SecurityFilterAutoConfiguration.class
})

@DisplayName("FreeBoardController 슬라이스 테스트")
class FreeBoardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // Service는 로드 안 됨 → Mock으로 대체
    @MockitoBean  // Spring Boot 3.4+ (@MockBean: 이전 버전)
    private FreeBoardService boardService;

    // JwtAuthFilter가 필요한 Bean들을 Mock으로 등록
    @MockitoBean
    JwtAuthFilter jwtAuthFilter;
    @MockitoBean
    JwtUtil jwtUtil;
    @MockitoBean
    MemberService memberService;

    // 테스트 시작 시 Bean 목록 출력
    @Autowired
    ApplicationContext context;

    @Test
    void checkBeans() {
        // FreeBoardController Bean 등록됐는지 확인
        String[] beans = context.getBeanNamesForType(
                FreeBoardController.class);
        System.out.println(">>> Controller Bean: "
                + Arrays.toString(beans));
        // [] 면 → Bean 등록 안 됨
    }

    @Test
    @DisplayName("GET /freeboard/list → 200 + 리스트 반환")
    void getList_success() throws Exception {
        // given
        List<FreeBoardDTO> boards = List.of(
                FreeBoardDTO.builder().bno(1L)
                        .title("스프링").writer("홍길동").build(),
                FreeBoardDTO.builder().bno(2L)
                        .title("JPA").writer("김철수").build()
        );
        given(boardService.getList()).willReturn(boards);

        // when & then
        mockMvc.perform(get("/freeboard/list")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("스프링"))
                .andExpect(jsonPath("$[1].title").value("JPA"))
                .andDo(result -> {
                    System.out.println(">>> Status: "
                            + result.getResponse().getStatus());
                    System.out.println(">>> Handler: "
                            + result.getHandler());
                    System.out.println(">>> Body: "
                            + result.getResponse().getContentAsString());
                });
    }

    @Test
    @DisplayName("POST /freeboard/register → 201 Created")
    void register_success() throws Exception {
        // given
        FreeBoardDTO inputDto = FreeBoardDTO.builder()
                .title("새 글").content("내용")
                .writer("홍길동").build();
        FreeBoardDTO savedDto = FreeBoardDTO.builder()
                .bno(1L).title("새 글").build();
        given(boardService.register(any())).willReturn(savedDto);

        // when & then
        mockMvc.perform(post("/freeboard/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    @DisplayName("GET /freeboard/detail?bno=999 → 404")
    void getDetail_notFound() throws Exception {
        // given
        given(boardService.f_detail(999L)).willReturn(null);

        // when & then
        mockMvc.perform(get("/freeboard/detail")
                        .param("bno", "999"))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("PUT /freeboard/modify → 200")
    void modify_success() throws Exception {
        // given
        FreeBoardDTO updateDto = FreeBoardDTO.builder()
                .bno(1L).title("수정 제목").content("수정 내용")
                .build();
        willDoNothing().given(boardService).modify(any());

        // when & then
        mockMvc.perform(put("/freeboard/modify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("DELETE /freeboard/remove?bno=1 → 200")
    void remove_success() throws Exception {
        // given
        willDoNothing().given(boardService).remove(1L);

        // when & then
        mockMvc.perform(delete("/freeboard/remove")
                        .param("bno", "1"))
                .andExpect(status().isOk())
                .andDo(print());
    }
}

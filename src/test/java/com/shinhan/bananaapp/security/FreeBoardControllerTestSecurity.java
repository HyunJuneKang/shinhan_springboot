package com.shinhan.bananaapp.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shinhan.bananaapp.replyprac.controller.FreeBoardController;
import com.shinhan.bananaapp.replyprac.dto.FreeBoardDTO;
import com.shinhan.bananaapp.replyprac.service.FreeBoardService;
import com.shinhan.bananaapp.security.jwt.JwtUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc // addFilters=false 없음 → 보안 필터가 실제로 동작
@WebMvcTest(controllers = FreeBoardController.class)
@Import(SecurityConfig.class)
@DisplayName("FreeBoardController 슬라이스 테스트 (보안 반영)")
class FreeBoardControllerTestSecurity {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // Service는 로드 안 됨 → Mock으로 대체
    @MockitoBean
    private FreeBoardService boardService;

    @MockitoBean
    MemberService memberService;
    @MockitoBean
    JwtUtil jwtUtil;
    // JwtAuthFilter는 @Component이고 SecurityConfig가 apiSecurity 체인에
    // 실제 빈으로 등록해 쓰므로 별도 @MockitoBean 없이 그대로 로드된다.

    @Autowired
    ApplicationContext context;

    @Test
    void checkBeans() {
        String[] beans = context.getBeanNamesForType(FreeBoardController.class);
        System.out.println(">>> Controller Bean: " + Arrays.toString(beans));
        // [] 면 → Bean 등록 안 됨
    }

    @Test
    @DisplayName("Authorization 헤더 없이 요청 → 403 (Http403ForbiddenEntryPoint 기본값)")
    void noToken_unauthorized() throws Exception {

        // ── when & then ─────────────────────────────
        mockMvc.perform(get("/freeboard/list"))
                .andExpect(status().isForbidden())
                .andDo(print());

    }

    @Test
    @DisplayName("유효한 토큰으로 요청 → 정상 처리")
    void validToken_ok() throws Exception {

        // ── given ────────────────────────────────────
        given(jwtUtil.validateToken("fake-valid-token")).willReturn(true);
        given(jwtUtil.getUserId("fake-valid-token")).willReturn("hong");
        given(boardService.getList()).willReturn(Collections.emptyList());

        // JwtAuthFilter가 userId로 memberService.loadUserByUsername()을 호출해
        // UserDetails를 받아와야 SecurityContext에 인증이 채워진다.
        // 이걸 stub 안 하면 details == null → 인증 미설정 → 여전히 401/403.
        UserDetails hongDetails = mock(UserDetails.class);
        given(hongDetails.getAuthorities()).willReturn(List.of()); // null이면 NPE
        given(memberService.loadUserByUsername("hong")).willReturn(hongDetails);

        given(boardService.getList()).willReturn(Collections.emptyList());

        // ── when & then ─────────────────────────────
        mockMvc.perform(get("/freeboard/list")
                        .header("Authorization", "Bearer fake-valid-token"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithMockUser // 인증된 사용자로 요청 → JwtAuthFilter는 이미 인증된 컨텍스트를 보고 스킵
    @DisplayName("GET /freeboard/list → 200 + 리스트 반환")
    void getList_success() throws Exception {
        // given
        List<FreeBoardDTO> boards = List.of(
                FreeBoardDTO.builder().bno(1L).title("스프링").writer("홍길동").build(),
                FreeBoardDTO.builder().bno(2L).title("JPA").writer("김철수").build()
        );
        given(boardService.getList()).willReturn(boards);

        // when & then
        mockMvc.perform(get("/freeboard/list")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("스프링"))
                .andExpect(jsonPath("$[1].title").value("JPA"))
                .andDo(print());
    }

    @Test
    @WithMockUser
    @DisplayName("POST /freeboard/register → 201 Created")
    void register_success() throws Exception {
        // given
        FreeBoardDTO inputDto = FreeBoardDTO.builder()
                .title("새 글").content("내용").writer("홍길동").build();
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
    @WithMockUser
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
    @WithMockUser
    @DisplayName("PUT /freeboard/modify → 200")
    void modify_success() throws Exception {
        // given
        FreeBoardDTO updateDto = FreeBoardDTO.builder()
                .bno(1L).title("수정 제목").content("수정 내용").build();
        willDoNothing().given(boardService).modify(any());

        // when & then
        mockMvc.perform(put("/freeboard/modify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithMockUser
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

//package com.shinhan.bananaapp.crosscuttingprac.filter;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.servlet.http.HttpSession;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//import java.util.List;
//
//@Slf4j
//public class LoginCheckFilter extends OncePerRequestFilter {
//    // ── 로그인 없이 접근 가능한 경로 ──────────────────────
//    private static final List<String> WHITE_LIST = List.of(
//            "/auth/signup",
//            "/auth/login",
//            "/auth/joinProc",
//            "/css/",
//            "/js/",
//            "/images/",
//            "/.well-known/"     // Chrome DevTools 자동 요청
//    );
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request,
//                                    HttpServletResponse response,
//                                    FilterChain filterChain)
//            throws ServletException, IOException {
//
//        String uri = request.getRequestURI();
//
//        // ① WHITE_LIST 경로는 체크 없이 통과
//        if (isWhiteListed(uri)) {
//            log.debug("[LoginCheckFilter] WHITE_LIST 통과: {}", uri);
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        // ② 세션에서 로그인 정보 확인
//        //    getSession(false) — 세션이 없으면 새로 만들지 않음
//        //    getSession() : default 는 getSession(true), session 이 있으면 얻기 , 없으면 만들기
//        HttpSession session = request.getSession(false);
//        boolean isLoggedIn = (session != null
//                && session.getAttribute("loginMember") != null);
//
//        if (!isLoggedIn) {
//            // ③ 미로그인 → 로그인 페이지로 리다이렉트
//            //    redirectURL 파라미터로 로그인 후 원래 페이지로 돌아오기 가능
//            log.info("[LoginCheckFilter] 미로그인 접근 차단: {}", uri);
//            response.sendRedirect("/auth/login?redirectURL=" + uri);
//            return;
//        }
//
//        // ④ 로그인 상태 → 다음 필터 또는 Controller로 진행
//        log.debug("[LoginCheckFilter] 로그인 확인 완료: {}", uri);
//        filterChain.doFilter(request, response);
//    }
//
//    /**
//     * WHITE_LIST에 포함된 경로인지 확인
//     * startsWith로 체크 — /css/style.css → /css/ 포함
//     */
//    private boolean isWhiteListed(String uri) {
//        return WHITE_LIST.stream()
//                .anyMatch(uri::startsWith);
//    }
//
//
//}

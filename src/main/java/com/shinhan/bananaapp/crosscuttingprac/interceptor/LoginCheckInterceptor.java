package com.shinhan.bananaapp.crosscuttingprac.interceptor;

import com.shinhan.bananaapp.mybatisprac.prev.MemberDTO;
import com.shinhan.bananaapp.mybatisprac.jdbc.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
//@Component
@RequiredArgsConstructor
public class LoginCheckInterceptor implements HandlerInterceptor {

    private final MemberService memberService;  // DB 조회 가능
//    private final BlackListRepository blackListRepo; // 차단 목록 조회 가능

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        MemberDTO member = memberService.login(MemberDTO.builder().email("abcd").password("1234")
                .mRole("manager").build());
//        log.info(member.toString());

        String uri = request.getRequestURI();
//        log.debug("[Interceptor] 요청 URI: {}", uri);

        HttpSession session = request.getSession(false);
        boolean isLoggedIn = (session != null
                && session.getAttribute("loginMember") != null);

        if (!isLoggedIn) {
//            log.info("[Interceptor] 미로그인 접근 차단: {}", uri);
            response.sendRedirect("/auth/login?redirectURL=" + uri);
            return false;  // 요청 차단
        }
        return true;  // 진행
    }
}

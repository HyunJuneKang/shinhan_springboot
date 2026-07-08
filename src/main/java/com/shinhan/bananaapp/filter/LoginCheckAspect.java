package com.shinhan.bananaapp.filter;

import com.shinhan.bananaapp.annotation.LoginRequired;
import com.shinhan.bananaapp.dto.MemberDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoginCheckAspect {
    private final HttpServletRequest request;
    public LoginCheckAspect(HttpServletRequest request) {
        this.request = request;
    }
    /**
     * @LoginRequired 붙은 메서드 실행 전
     * 세션에서 권한 확인
     */
    //PointCut : execution(* com.shinhan.bananaapp.* add(...)
    //         : within(* com.shinhan.bananaapp.AA)

    @Before("@annotation(loginRequired)")
    public void checkRole(JoinPoint joinPoint,
                          LoginRequired loginRequired) {
        String methodName = joinPoint.getSignature().getName();
        // 세션에서 로그인 정보 조회
        // Interceptor를 통과한 시점이므로 loginMember는 반드시 존재
        HttpSession session = request.getSession(false);
        MemberDTO loginMember =
                (MemberDTO) session.getAttribute("loginMember");
        // 권한 확인 (role이 지정된 경우만)
        String requiredRole = loginRequired.role();
        if (!requiredRole.isEmpty()
                && !requiredRole.equals(loginMember.getMRole())) {
            log.warn("[AOP] 권한 없음 → {} / 필요: {} / 현재: {} / 사용자: {}",
                    methodName,
                    requiredRole,
                    loginMember.getMRole(),
                    loginMember.getEmail());
            throw new RuntimeException(
                    requiredRole + " 권한이 필요합니다.");
        }
        log.info("[AOP] 권한 확인 완료 → {} / 사용자: {} ({})",
                methodName,
                loginMember.getEmail(),
                loginMember.getMRole());
    }
}
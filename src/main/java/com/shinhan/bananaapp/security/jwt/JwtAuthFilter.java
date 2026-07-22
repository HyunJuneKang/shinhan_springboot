package com.shinhan.bananaapp.security.jwt;

import com.shinhan.bananaapp.security.MemberService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final MemberService memberService;  // UserDetailsService 구현체
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // 이미 인증된 경우 skip
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            filterChain.doFilter(request, response);
            return;
        }

        // 인증 불필요 URL skip
        List<String> skipUrls = List.of(
                "/api/auth/login", "/api/auth/refresh", "/api/auth/joinProc"
        );
        if (skipUrls.stream().anyMatch(url ->
                request.getRequestURI().startsWith(url))) {
            filterChain.doFilter(request, response);
            return;
        }

        // Authorization 헤더에서 Bearer 토큰 추출
        // 표준 형식: Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token  = authHeader.substring(7); // "Bearer " (7글자) 제거
            if (jwtUtil.validateToken(token)) {
                String userId       = jwtUtil.getUserId(token);
                UserDetails details = memberService.loadUserByUsername(userId);
                if (details != null) {
                    // SecurityContext에 인증 객체 등록
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    details, null, details.getAuthorities());
                    SecurityContextHolder.getContext()
                            .setAuthentication(authToken);
                    // 이후 @AuthenticationPrincipal, Principal,
                    // SecurityContextHolder 사용 가능
                }
            }
        } catch (Exception e) {
            log.debug("JWT 인증 실패: {}", e.getMessage());
            // 만료/서명 오류 → 401은 Security가 자동 처리
        }
        filterChain.doFilter(request, response);
    }
}

package com.shinhan.bananaapp.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
//@Component......filter Config에서 직접 생성하여 등록 따라서 불필요
public class RequestLoggingFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String uri = request.getRequestURI();

        if (uri.startsWith("/.well-known")) {
            filterChain.doFilter(request, response);
            return;
        }

        long start = System.currentTimeMillis();
        String method = request.getMethod();

        log.info("[REQUEST order2]  {} {}", method, uri);
        filterChain.doFilter(request, response);  // 다음 필터 or 서블릿으로 전달
        long elapsed = System.currentTimeMillis() - start;
        log.info("[RESPONSE order2] {} {} → {} ({}ms)",
                method, uri, response.getStatus(), elapsed);
    }
}

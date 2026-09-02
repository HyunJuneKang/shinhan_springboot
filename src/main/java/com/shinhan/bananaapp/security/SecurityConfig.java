package com.shinhan.bananaapp.security;

import com.shinhan.bananaapp.security.jwt.JwtAuthFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    // 인증 없이 접근 가능한 경로 목록
    private final String[] WHITE_LIST = {
            "/security/all", "/auth/signup", "/auth/joinProc","/auth/login",
            "/images/**", "/css/**", "/js/**", "/favicon.ico",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/webjars/**","/swagger-ui.html",
            "/freeboard/**"
    };


    // Spring MVC 매핑 정보를 가진 Bean 주입
    private final RequestMappingHandlerMapping requestMappingHandlerMapping;

    // ① API 체인 — JWT 인증 (Order 1, 먼저 매칭)
    @Bean
    @Order(1)
    SecurityFilterChain apiSecurity(
            HttpSecurity http,
            JwtAuthFilter jwtAuthFilter
    ) throws Exception {

        http
                // 이 체인은 /api/** 요청에만 적용
                .securityMatcher("/api/**")
                // JSON 기반 JWT API이므로 CSRF 비활성화
                .csrf(AbstractHttpConfigurer::disable)

                // 세션에 인증정보를 저장하지 않음
                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )
                // 폼 로그인과 Basic 인증 사용 안 함
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                // 미인증 API 요청은 로그인 페이지가 아니라 401 JSON 반환
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(
                                (request, response, authException) -> {
                                    response.setStatus(
                                            HttpServletResponse.SC_FORBIDDEN
                                    );
                                    response.setContentType(
                                            "application/json;charset=UTF-8"
                                    );
                                    response.getWriter().write(
                                            "{\"message\":\"인증이 필요합니다.\"}"
                                    );
                                }
                        )
                        .accessDeniedHandler(
                                (request, response, accessDeniedException) -> {
                                    response.setStatus(
                                            HttpServletResponse.SC_FORBIDDEN
                                    );
                                    response.setContentType(
                                            "application/json;charset=UTF-8"
                                    );
                                    response.getWriter().write(
                                            "{\"message\":\"접근 권한이 없습니다.\"}"
                                    );
                                }
                        )
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/auth/login",
                                "/api/auth/refresh",
                                "/api/auth/joinProc"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(
                        jwtAuthFilter,
                        UsernamePasswordAuthenticationFilter.class
                );
        return http.build();
    }
    @Bean @Order(2)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // 1. 인증, 인가 설정 — URL별 접근 권한
        //requestMatchers는 URL pattern
//        http.authorizeHttpRequests(auth -> auth
//                .requestMatchers(WHITE_LIST).permitAll() //무조건허용
//                .requestMatchers("/admin/**").hasRole("ADMIN")
//                .requestMatchers("/manager/**").hasAnyRole("ADMIN", "MANAGER")
//                .anyRequest().authenticated()  //나머지는 반드시 인증되어야 자원사용가능
//        );

        // 2. CSRF 비활성화 (REST API + JWT 방식)
        http.csrf(AbstractHttpConfigurer::disable);
        // 3. 폼 로그인 설정...default로 security 제공하는 페이지아닌 개발된 page로 변경
//        http.formLogin(login -> login
//                .loginPage("/auth/login")      //post는 자동처리
//                .usernameParameter("mid")        // 폼의 name 속성값
//                .passwordParameter("mpassword")  // 폼의 name 속성값
//                .successHandler((request, response, authentication) -> {
//                    response.sendRedirect("/auth/loginSuccess");
//                })
//                .failureHandler((request, response, exception) -> {
//                    request.getSession().setAttribute("loginError", "로그인 실패");
//                    response.sendRedirect("/auth/login");
//                })
//                .permitAll()
//        );

        // 4. 로그아웃 설정
        http.logout(out -> out
                .logoutUrl("/auth/logout")   //security 기본제공 주소를 변경
                .logoutSuccessUrl("/auth/login")
                .invalidateHttpSession(true)    // 세션 무효화
                .deleteCookies("JSESSIONID")   // 쿠키 삭제
        );

        // 5. 403 접근 거부 페이지, 404, .....
        //http.exceptionHandling(a->a.accessDeniedPage(""))
        http.exceptionHandling(handling -> handling
                .accessDeniedHandler((request, response, ex) -> {
                    String uri = request.getRequestURI();
                    // 실제 URL이 존재하는지 확인
                    boolean urlExists = isUrlMapped(uri, request);
                    if (!urlExists) {
                        // URL 자체가 없음 → 404
                        response.sendError(
                                HttpServletResponse.SC_NOT_FOUND);
                    } else {
                        // URL은 있는데 권한 없음 → 403
                        response.sendRedirect("/auth/accessDenied");
                    }
                })
        );
        return http.build();
    }


    // URL이 Controller에 매핑되어 있는지 확인
    private boolean isUrlMapped(
            String uri, HttpServletRequest request) {
        try {
            return requestMappingHandlerMapping.getHandler(request) != null;
        } catch (Exception e) {
            return false;  // 매핑 없음 → false
        }
    }

}
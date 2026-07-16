//package com.shinhan.bananaapp.config;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
////@Configuration
//@RequiredArgsConstructor
//public class WebConfig implements WebMvcConfigurer {
//
//    private final LoginCheckInterceptor loginCheckInterceptor;
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(loginCheckInterceptor)
//                .addPathPatterns("/**")        // 모든 경로에 적용
//                .excludePathPatterns(          // 제외 경로
//                        "/auth/login", "/auth/logout", "/auth/signup",
//                        "/css/**", "/js/**", "/images/**", "/media/**"
//                );
//    }
//}

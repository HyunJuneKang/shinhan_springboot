package com.shinhan.bananaapp.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LoginRequired {
    // 필요 권한 (기본값: 빈 문자열 = 로그인만 확인)
    String role() default "";
}
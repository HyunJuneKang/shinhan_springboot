package com.shinhan.bananaapp.config;

import com.shinhan.bananaapp.filter.EncodingFilter;
import com.shinhan.bananaapp.filter.LoginCheckFilter;
import com.shinhan.bananaapp.filter.RequestLoggingFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

@Slf4j
//@Configuration
public class FilterConfig {

//    @Bean
    public FilterRegistrationBean<EncodingFilter> encodingFilter() {
        FilterRegistrationBean<EncodingFilter> bean =
                new FilterRegistrationBean<>();

        bean.setFilter(new EncodingFilter());
        bean.setOrder(1);
        bean.addUrlPatterns("/auth/*");
        return bean;
    }

    @Bean
    public FilterRegistrationBean<RequestLoggingFilter> loggingFilter() {
        FilterRegistrationBean<RequestLoggingFilter> bean =
                new FilterRegistrationBean<>();
        bean.setFilter(new RequestLoggingFilter());
        bean.setOrder(2);
        bean.addUrlPatterns("/*");
        return bean;
    }

//    @Bean
    public FilterRegistrationBean<LoginCheckFilter> loginCheckFilter(){
        FilterRegistrationBean<LoginCheckFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new LoginCheckFilter());
        bean.setOrder(3);               // 로깅 다음에 실행
        bean.addUrlPatterns("/*");      // 전체 경로 (WHITE_LIST는 필터 내부에서 처리)
        bean.setName("loginCheckFilter");
        return bean;
    }


}

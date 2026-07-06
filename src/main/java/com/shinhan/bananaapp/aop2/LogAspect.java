package com.shinhan.bananaapp.aop2;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect       // AOP 클래스 선언 = @PointCut + 시점(Before,After,After...,Around) + Advice(보조업무)
@Component    // Spring Bean 등록
@Order(0)
public class LogAspect {
    // ── Pointcut 정의 ──────────────────────────────────────────────
    // execution() : service 패키지의 모든 클래스·모든 메서드
    //모든패키지의 f_1()메서드에 적용하는법
    @Pointcut("execution(* f_1())")
    public void allMethods2() {
    }

    @Pointcut("execution(* com.shinhan.bananaapp.aop2.*.*(..))")
    public void allMethods() {
    }
    // within() : CalculatorImpl 클래스 안의 모든 메서드
    @Pointcut("within(com.shinhan.bananaapp.aop2.CalculatorImpl)")
    public void calculatorOnly() {
    }
    // ── @Before ────────────────────────────────────────────────────
    @Before("allMethods()")
    public void beforeLog(JoinPoint jp) {
        System.out.println("[Before1] 메서드 : " + jp.getSignature().getName());
        System.out.println("[Before1] 파라미터: " + Arrays.toString(jp.getArgs()));
    }
    // ── @AfterReturning ────────────────────────────────────────────
    @AfterReturning(pointcut = "allMethods()", returning = "result")
    public void afterReturningLog(JoinPoint jp, Object result) {
        System.out.println(Arrays.toString(jp.getArgs()));
        System.out.println("order(0)[AfterReturning] 반환값 : " + result);
    }
    // ── @AfterReturning ────────────────────────────────────────────
    @AfterReturning(pointcut = "allMethods2()", returning = "result")
    public void afterReturningLog2(JoinPoint jp, Object result) {
        System.out.println(Arrays.toString(jp.getArgs()));
        System.out.println("[AfterReturning] 반환값 : " + result);
    }
    // ── @AfterThrowing ─────────────────────────────────────────────
    @AfterThrowing(pointcut = "allMethods()", throwing = "ex")
    public void afterThrowingLog(JoinPoint jp, Exception ex) {
        System.out.println("[AfterThrowing] 예외 : " + ex.getMessage());
    }
    // ── @Around (within 사용) ──────────────────────────────────────
    @Around("calculatorOnly()")
    public Object aroundLog(ProceedingJoinPoint pjp) throws Throwable {
        System.out.println("order(0)사용된 메서드 이름 " + pjp.getSignature().getName());
        long start = System.currentTimeMillis();
        System.out.println("order(0)[Around-Before] 실행 시작");
        Object result = pjp.proceed();  // 실제 메서드 실행
        long end = System.currentTimeMillis();
        System.out.println("order(0)[Around-After]  실행 시간 : " + (end - start) + "ms");
        return result;
    }
}

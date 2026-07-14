package com.shinhan.bananaapp.crosscuttingprac.aopprac.aop2;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Component
@Aspect
@Order(1)
public class StopWatchAdvice {

    // a로 시작하고 (int, int) 파라미터인 메서드에만 적용
    @Pointcut("execution(* a*(int, int))")
    public void targetMethod() {} // 함수이름은 사용자가 정한다

    @Around("targetMethod()")
    public Object aroundMethod(ProceedingJoinPoint jp) throws Throwable {

        System.out.println("****** " + jp.getSignature().getName()
                + " order(1)메서드 호출 전(StopWatchAdvice)");

        // 보조업무 — 시간 측정 시작
        StopWatch watch = new StopWatch("계산시간");
        watch.start();

        // 주업무 수행
        Object object = jp.proceed();

        // 보조업무 — 시간 측정 종료
        System.out.println("****** " + jp.getSignature().getName()
                + " order(1)메서드 호출 후(StopWatchAdvice)");
        watch.stop();
        System.out.println("order(1)주업무를 수행하는데 걸리는 시간: "
                + watch.getTotalTimeMillis() + "ms");
        System.out.println(watch.prettyPrint());

        return object;
    }
}
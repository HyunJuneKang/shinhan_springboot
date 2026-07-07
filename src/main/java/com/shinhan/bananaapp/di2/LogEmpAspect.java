package com.shinhan.bananaapp.di2;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogEmpAspect {
    @Pointcut("execution(* com.shinhan.bananaapp.di2.EmpService.*(..))")
    public void empAllMethods(){
    }
    @Around("empAllMethods()")
    public Object logEmpAllMethods(ProceedingJoinPoint pjp) throws Throwable {
        long start = System.nanoTime();
        System.out.println(pjp.getTarget().getClass().getName());
        System.out.println("실행 시작");
        Object result = pjp.proceed();
        long end = System.nanoTime();
        System.out.println("걸린시간: " + (end - start) + "ms");
        return result;
    }
}

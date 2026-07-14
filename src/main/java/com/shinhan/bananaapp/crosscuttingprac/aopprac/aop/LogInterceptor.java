package com.shinhan.bananaapp.crosscuttingprac.aopprac.aop;


import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class LogInterceptor implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        System.out.println("method" + invocation.getMethod() +" is called on" +
                invocation.getThis() +"with args " + invocation.getArguments());
        Object ret = invocation.proceed(); //핵심로직을 반드시 실행하고 결과를 반드시 return;
        System.out.println("method" + invocation.getMethod() +" returns "+ret);
        return ret;
    }
}

package com.example.studymanagmentapp.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class RetryAspect {

    @Pointcut("@annotation(com.example.studymanagmentapp.aspect.Retry) || @within(com.example.studymanagmentapp.aspect.Retry)")
    public void retryPointCut() {}

    @Around("retryPointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Retry retry = null;
        Signature signature = joinPoint.getSignature();

        if (signature instanceof MethodSignature ms) {
            Method method = ms.getMethod();
            retry = method.getAnnotation(Retry.class);
            if (retry == null) {
                Class<?> declaringType = signature.getDeclaringType();
                retry = declaringType.getAnnotation(Retry.class);
            }
        }

        int times = retry.times();
        long waitTime = retry.waitTime();

        if (times <= 0) times = 1;

        for (int i = 1; i <= times; i++) {
            System.out.println("Retry " + i);
            try {
                return joinPoint.proceed();
            } catch (Exception e) {
                if (i == times) throw e;
                if (waitTime > 0)
                    Thread.sleep(waitTime);
            }
        }
        return null;
    }
}

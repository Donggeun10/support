package com.example.support.advice;

import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class ControllerAspect {

    @Around("execution(* com.example.support.controller.*.*(..))")
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        Object result = joinPoint.proceed();
        long endTime = System.currentTimeMillis();

        String controllerName = joinPoint.getSignature().toShortString();
        if (log.isDebugEnabled() || endTime - startTime > 500) {
            log.info("{}:{}ms params:{}", controllerName, (endTime - startTime), getParameters(joinPoint));
        }
        return result;
    }

    private Map<String, Object> getParameters(final ProceedingJoinPoint joinPoint) {
        final String[] parameterNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
        final Object[] arguments = joinPoint.getArgs();

        Map<String, Object> parameterMap = new HashMap<>();

        for (int i = 0; i < parameterNames.length; i++) {
            parameterMap.put(parameterNames[i], arguments[i]);
        }

        return parameterMap;
    }

}

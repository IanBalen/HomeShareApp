package com.homeshare.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import static com.homeshare.util.Helper.asJsonString;
import static com.homeshare.util.Helper.log;


@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Before("within((com.homeshare..*..*Controller))")
    public void logBefore(JoinPoint joinPoint) {
        log("REQUEST RECEIVED - " + getRequest(joinPoint));
    }

    private String getRequest(JoinPoint joinPoint) {
        return joinPoint.getSignature().getName() + arrToString(joinPoint.getArgs());
    }

    private String arrToString(Object[] args) {
        if(args == null || args.length == 0 || args[0] == null) {
            return "";
        }
        return " - parametars: " + asJsonString(args);
    }

    @AfterReturning(pointcut = "within((com.homeshare..*..*Controller))", returning = "result")
    public void logAfter(JoinPoint joinPoint, Object result) {
        log("RESPONSE RETURNED - " + getResponse(joinPoint, result));
    }

    private String getResponse(JoinPoint joinPoint, Object result) {
        return joinPoint.getSignature().getName() + resultToString(result);
    }

    private String resultToString(Object result) {
        if(result == null) {
            return "";
        }
        return " - result: " + asJsonString(result);
    }
}
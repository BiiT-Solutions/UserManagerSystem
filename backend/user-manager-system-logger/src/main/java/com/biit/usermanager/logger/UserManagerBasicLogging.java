package com.biit.usermanager.logger;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

/**
 * Logs all file managed by Spring. In this project only are DAOs.
 */
@Aspect
@Component
public class UserManagerBasicLogging extends AbstractLogging {

    /**
     * Following is the definition for a pointcut to select all the methods
     * available. So advice will be called for all the methods.
     */
    @Pointcut("execution(* com.biit.usermanager.rest.api..*(..))")
    private void selectAll() {
    }

    /**
     * Using an existing annotation.
     */
    @Pointcut("@annotation(org.springframework.transaction.annotation.Transactional)")
    public void isAnnotated() {
    }

    /**
     * Using custom annotation.
     *
     * @param auditable if is auditable
     */
    @Pointcut("@annotation(auditable)")
    public void isAuditable(Auditable auditable) {
    }

    /**
     * This is the method which I would like to execute before a selected method
     * execution.
     *
     * @param joinPoint the joinPoint
     */
    @Before(value = "selectAll() || isAnnotated()")
    public void beforeAdvice(JoinPoint joinPoint) {

    }

    @Around(value = "selectAll() || isAnnotated()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        final StopWatch stopWatch = new StopWatch();
        final Object returnValue;
        stopWatch.start();
        returnValue = joinPoint.proceed();
        stopWatch.stop();
        log(stopWatch.getTotalTimeMillis(), joinPoint);
        return returnValue;
    }

    /**
     * This is the method which I would like to execute after a selected method
     * execution.
     */
    @After(value = "selectAll() || isAnnotated()")
    public void afterAdvice() {
    }

    /**
     * This is the method which I would like to execute when any method returns.
     *
     * @param retVal the returning value.
     */
    @AfterReturning(pointcut = "selectAll() || isAnnotated()", returning = "retVal")
    public void afterReturningAdvice(Object retVal) {
        if (retVal != null) {
            log("Returning: '{}' ", retVal.toString());
        } else {
            log("Returning: 'void'.");
        }
    }

    /**
     * This is the method which I would like to execute if there is an exception
     * raised by any method.
     *
     * @param ex the exception
     */
    @AfterThrowing(pointcut = "selectAll() || isAnnotated()", throwing = "ex")
    public void afterThrowingAdvice(IllegalArgumentException ex) {
        log("There has been an exception: '{}' ", ex.getMessage());
    }

}

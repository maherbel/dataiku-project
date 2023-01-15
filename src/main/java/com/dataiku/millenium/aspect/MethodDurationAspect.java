package com.dataiku.millenium.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/*
 * MethodDurationAspect class is used to measure and log the execution time of all methods in the package services.
 */
@Aspect
@Component
public class MethodDurationAspect {

    /*
     * Logger for this class, it can be used to log messages for debugging, info, error, and so on
     */
    private static final Logger logger = LoggerFactory.getLogger(MethodDurationAspect.class);

    /*
     * @Around annotation is used to define an advice that will be executed around a method matching the pointcut.
     * The pointcut is defined using the execution expression, it matches all methods in the package services.
     * The advice is to measure the execution time of the method and log it.
     */

    /**
     * @param joinPoint defined using the execution expression, it matches all methods in the package services.
     * @return
     * @throws Throwable
     * @Around annotation is used to define an advice that will be executed around a method matching the pointcut.
     * The pointcut is defined using the execution expression, it matches all methods in the package services.
     * The advice is to measure the execution time of the method and log it.
     */
    @Around("execution(* com.dataiku.millenium.services.*.*(..))")
    public Object logDuration(ProceedingJoinPoint joinPoint) throws Throwable {
        // Start time of method execution
        long start = System.currentTimeMillis();
        // Method name
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getSignature().getDeclaringType().getName();

        try {
            // Proceed with the method call
            Object result = joinPoint.proceed();
            // Return the result of the method
            return result;
        } finally {
            // End time of method execution
            long duration = System.currentTimeMillis() - start;
            // Log the execution time of the method
            logger.info("Method {} in class {} took {} ms", methodName, className, duration);
        }
    }
}


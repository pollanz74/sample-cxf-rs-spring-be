package org.pollanz.samples.api.gateway.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.pollanz.samples.api.gateway.utils.ResponseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import javax.ws.rs.core.Response;

@Aspect
public class ResponseAspect {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private Environment env;

    /**
     * Pointcut that matches all Web REST endpoints.
     */
    @Pointcut("within(org.pollanz.samples.api.gateway.impl.core.proxy..*) ")
    public void responseForwardPointcut() {
        // Method is empty as this is just a Poincut, the implementations are in the advices.
    }

    /**
     * Advice that logs when a method is entered and exited.
     */
    @Around("responseForwardPointcut()")
    public Object responseForwardAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed();
        if (result instanceof Response) {
            return ResponseUtils.forward((Response) result);
        }
        return result;
    }
}

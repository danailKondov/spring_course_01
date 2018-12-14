package ru.otus.spring01.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Created by хитрый жук on 11.12.2018.
 */
@Aspect
@Component
public class LoggingAspect {

    private static final Logger log = LoggerFactory.getLogger("");

    @Before("within(ru.otus.spring01..*)")
    public void logBefore(JoinPoint joinPoint) throws Throwable {
        log.info("Вызов метода: " + joinPoint.getSignature().toShortString() +
                "с параметрами: " + Arrays.toString(joinPoint.getArgs()));
    }
}

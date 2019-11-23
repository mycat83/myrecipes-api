package link.myrecipes.api.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class LoggerAspect {
    @Before("execution(* link.myrecipes.api.controller..*.*(..))")
    public void controllerLogger(JoinPoint joinPoint) {
        log.info("controllerLogger!!");
        log.info(LocalDateTime.now().toString());
        log.info(joinPoint.getSignature().toShortString());
        log.info(Arrays.toString(joinPoint.getArgs()));
    }

    @AfterThrowing(value = "execution(* link.myrecipes.api.controller..*.*(..))", throwing = "exception")
    public void controllerFailLogger(JoinPoint joinPoint, Exception exception) {
        log.info("controllerFailLogger!!");
        log.info(LocalDateTime.now().toString());
        log.info(joinPoint.getSignature().toShortString());
        log.info(exception.getClass().getSimpleName());
        log.info(exception.getMessage());
    }
}

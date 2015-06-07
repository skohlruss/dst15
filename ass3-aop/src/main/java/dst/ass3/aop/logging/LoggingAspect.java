package dst.ass3.aop.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;

import java.lang.reflect.Field;
import java.util.logging.Logger;

@Aspect
public class LoggingAspect {

    private static final String MSG_BEFORE = " started to execute";
    private static final String MSG_AFTER = " is finished";


    @Before("execution(void dst.ass3.aop.IPluginExecutable.execute()) && !@annotation(dst.ass3.aop.logging.Invisible)")
    public void beforeExecute(JoinPoint joinPoint) {
        log(MSG_BEFORE, joinPoint.getTarget());
    }

    @After("execution(void dst.ass3.aop.IPluginExecutable.execute()) && !@annotation(dst.ass3.aop.logging.Invisible)")
    public void logAfterExecution(JoinPoint joinPoint) {
        log(MSG_AFTER, joinPoint.getTarget());
    }

    private void log(String message, Object objectContainingLogger) {

        Logger logger = getLogger(objectContainingLogger);
        String wholeMessage = "Plugin " + objectContainingLogger.getClass().getCanonicalName() + message;

        if (logger != null) {
            logger.info(wholeMessage);
        } else {
            System.out.println(wholeMessage);
        }
    }

    private Logger getLogger(Object object) {

        Logger logger = null;
        try {
            for (Field field: object.getClass().getDeclaredFields()) {
                if (Logger.class.isAssignableFrom(field.getType())) {

                    field.setAccessible(true);
                    logger = (Logger)field.get(object);
                }
            }
        } catch (IllegalAccessException e) {
            System.out.println("Couldn't access logger");
        }

        return logger;
    }
}

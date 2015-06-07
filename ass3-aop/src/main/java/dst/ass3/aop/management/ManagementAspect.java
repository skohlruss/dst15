package dst.ass3.aop.management;

import dst.ass3.aop.IPluginExecutable;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

@Aspect
public class ManagementAspect {

    private static final String PLUGIN_METHOD_NAME = "execute";

    private Map<IPluginExecutable, TimerTask> timerTaskMap = new ConcurrentHashMap<>(new HashMap<IPluginExecutable, TimerTask>());


    @Before("execution(void dst.ass3.aop.IPluginExecutable.execute())")
    public void executionStart(JoinPoint joinPoint) {
        final IPluginExecutable pluginExecutable = (IPluginExecutable)joinPoint.getTarget();

        try {
            Method method = pluginExecutable.getClass().getMethod(PLUGIN_METHOD_NAME);
            Annotation timeoutAnnotation = method.getAnnotation(Timeout.class);
            if (timeoutAnnotation == null) {
                return;
            }

            TimerTask timerTask = getTimerTask(pluginExecutable);
            timerTaskMap.put(pluginExecutable, timerTask);
            new Timer().schedule(timerTask, ((Timeout) timeoutAnnotation).value());


        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @After("execution(void dst.ass3.aop.IPluginExecutable.execute())")
    public void executionEnds(JoinPoint joinPoint) {

        final IPluginExecutable pluginExecutable = (IPluginExecutable) joinPoint.getTarget();
        TimerTask timerTask = timerTaskMap.get(pluginExecutable);
        if (timerTask != null) {
            timerTask.cancel();
        }
    }

    private TimerTask getTimerTask(final IPluginExecutable pluginExecutable) {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                pluginExecutable.interrupted();
            }
        };

        return timerTask;
    }
}

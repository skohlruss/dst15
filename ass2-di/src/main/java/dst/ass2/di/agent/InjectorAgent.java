package dst.ass2.di.agent;

import dst.ass2.di.annotation.Component;
import javassist.*;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InjectorAgent implements ClassFileTransformer {

    private final static Logger logger = Logger.getLogger(ClassFileTransformer.class.getName());

    @Override
    public byte[] transform(ClassLoader loader, String className,
            Class<?> classBeingRedefined,
            ProtectionDomain protectionDomain,
            byte[] classfileBuffer) throws IllegalClassFormatException {

        ClassPool pool = ClassPool.getDefault();
        CtClass cl = null;

        try {
            cl = pool.makeClass(new java.io.ByteArrayInputStream(classfileBuffer));
        } catch (IOException e) {
            logger.log(Level.WARNING, "error make class: " + classBeingRedefined.getName());
        }

        if (cl.hasAnnotation(Component.class)) {
            try {

                /**
                 * If class is extended modify only super class, because its constructor
                 * is called every time
                 */
                if (cl.getSuperclass().hasAnnotation(Component.class)) {
                    return cl.toBytecode();
                }

                logger.info("Agent - modifying class: " + cl.getSimpleName());
                /**
                 * modify the constructor
                 */
                CtConstructor[] defaultConstructors = cl.getDeclaredConstructors();
                for (CtConstructor ctConstructor: defaultConstructors) {
                    ctConstructor.insertAfter("dst.ass2.di.InjectionControllerFactory.getStandAloneInstance().initialize(this);");
                }

                /**
                 * Add new constructor which is used at first place
                 */
                CtConstructor dummyConstructor = new CtConstructor(new CtClass[]{pool.get("dst.ass2.di.annotation.Inject")}, cl);
                dummyConstructor.setBody("{System.out.println(\"Injector agent constructor - empty\"); return;}");
                cl.addConstructor(dummyConstructor);

                return cl.toBytecode();

            } catch (NotFoundException e) {
                System.out.println("error");
                e.printStackTrace();
            } catch (CannotCompileException e) {
                System.out.println("error");
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println("error");
                e.printStackTrace();
            }
        }


        return classfileBuffer;
    }

    /**
     *  The agent class must implement a public static premain method similar in principle
     *  to the main application entry point. After the Java Virtual Machine (JVM) has initialized,
     *  each premain method will be called in the order the agents were specified, then the
     *  real application main method will be called. Each premain method must return in order
     *  for the startup sequence to proceed.
     */
    public static void premain(String agentArgs, Instrumentation inst) {
        ClassFileTransformer classFileTransformer = new InjectorAgent();
        inst.addTransformer(classFileTransformer);
    }
}

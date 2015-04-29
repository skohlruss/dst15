package dst.ass2.di.agent;

import dst.ass2.di.annotation.Component;
import javassist.*;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

public class InjectorAgent implements ClassFileTransformer {

//    public InjectorAgent(){}

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
            System.out.println("error");
        }

        if (cl.hasAnnotation(Component.class)) {
            System.out.println("has annotation");
            try {
                CtConstructor defaultConstructor = cl.getDeclaredConstructor(new CtClass[0]);
                defaultConstructor.insertAfter("dst.ass2.di.InjectionControllerFactory.getStandAloneInstance().initialize(this);");
                return cl.toBytecode();

            } catch (NotFoundException e) {
                System.out.println("error");
                e.printStackTrace();
            } catch (CannotCompileException e) {
                e.printStackTrace();
                System.out.println("error");
            } catch (IOException e) {
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

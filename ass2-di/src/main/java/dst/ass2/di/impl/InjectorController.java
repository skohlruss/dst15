package dst.ass2.di.impl;

import dst.ass2.di.IInjectionController;
import dst.ass2.di.InjectionException;
import dst.ass2.di.annotation.Component;
import dst.ass2.di.annotation.Inject;
import dst.ass2.di.model.ScopeType;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by pavol on 13.4.2015.
 */
public class InjectorController implements IInjectionController {


    Map<Class<?>, Object> singletonMap = new HashMap<>();

    @Override
    public void initialize(Object obj) throws InjectionException {

        Component component = obj != null ? obj.getClass().getAnnotation(Component.class) : null;
        if (component == null) {
            String className = obj != null ? obj.getClass().getCanonicalName() : "null";
            throw new InjectionException(className + " is null isn't annotated with: @Component");

        }

            for (Field field: obj.getClass().getDeclaredFields()) {
                initializeField(field);
            }
    }


    private void initializeField(Field field) throws InjectionException {
        Inject inject = field.getAnnotation(Inject.class);
        if (inject == null) {
            return;
        }

        Component fieldComponent = field.getClass().getAnnotation(Component.class);
        if (fieldComponent == null) {
            throw new InjectionException("Annotated field is not component" + field.getName());
        }

        // initialize field
        Class<?> fieldClass = field.getDeclaringClass();
        Object newFieldObject = null;
        if (singletonMap.get(fieldClass) == null) {
            try {
                newFieldObject = fieldClass.newInstance();
            } catch (InstantiationException e) {
                throw new InjectionException(e);
            } catch (IllegalAccessException e) {
                throw new InjectionException(e);
            }
        }
    }

    @Override
    public <T> T getSingletonInstance(Class<T> clazz) throws InjectionException {

        Component component = clazz != null ? clazz.getClass().getAnnotation(Component.class) : null;
        if (component == null) {
            String className = clazz != null ? clazz.getClass().getCanonicalName() : "null";
            throw new InjectionException(className + " is null isn't annotated with: @Component");
        }

        if (component.equals(ScopeType.PROTOTYPE)) {
            throw new InjectionException(clazz.getCanonicalName() + " getting singleton instance for PROTOTYPE bean");
        }

        T type = (T) singletonMap.get(clazz);
        if (type == null) {
            try {
                type = (T) clazz.newInstance();
            } catch (InstantiationException e) {
                throw new InjectionException(e);
            } catch (IllegalAccessException e) {
                throw new InjectionException(e);
            }

            initialize(type);
        }

        return type;
    }
}

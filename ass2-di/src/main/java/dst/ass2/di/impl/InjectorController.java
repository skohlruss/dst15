package dst.ass2.di.impl;

import dst.ass2.di.IInjectionController;
import dst.ass2.di.InjectionException;
import dst.ass2.di.annotation.Component;
import dst.ass2.di.annotation.ComponentId;
import dst.ass2.di.annotation.Inject;
import dst.ass2.di.model.ScopeType;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by pavol on 13.4.2015.
 */
public class InjectorController implements IInjectionController {

    private Map<Class<?>, Object> singletonMap = Collections.synchronizedMap(new HashMap<Class<?>, Object>());
    private AtomicLong idGenerator = new AtomicLong(0);


    @Override
    public void initialize(Object obj) throws InjectionException {

        Component component = obj != null ? obj.getClass().getAnnotation(Component.class) : null;
        if (component == null) {
            String className = obj != null ? obj.getClass().getCanonicalName() : "null";
            throw new InjectionException(className + " is null isn't annotated with: @Component");

        }

        /**
         * Exception is thrown in case double initialization of singleton
         */
        if (component.scope().equals(ScopeType.SINGLETON) && singletonMap.get(obj.getClass()) != null) {
            throw new InjectionException(obj.getClass() + " singleton initialized multiple times");
        }

        /**
         * Initialize
         */
        initializeFields(obj);

        /**
         * Singleton should be added to map
         */
        if (component.scope().equals(ScopeType.SINGLETON)) {
            singletonMap.put(obj.getClass(), obj);
        }
    }

    @Override
    public <T> T getSingletonInstance(Class<T> clazz) throws InjectionException {

        Component component = clazz != null ? clazz.getAnnotation(Component.class) : null;
        if (component == null) {
            String className = clazz != null ? clazz.getCanonicalName() : "null";
            throw new InjectionException(className + " is null isn't annotated with: @Component");
        }

        if (component.scope().equals(ScopeType.PROTOTYPE)) {
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


    private void initializeFields(Object obj) {

        Long id = null;
        for (Class<?> clazz = obj.getClass(); clazz.getSuperclass() != null; clazz = clazz.getSuperclass()) {
            /**
             * If super class is not annotated with @Component we should end
             */
            if (clazz.getAnnotation(Component.class) == null) {
                break;
            }

            id = initializeFieldsOfClass(obj, clazz, id);
        }
    }

    /**
     * Method is called from initializeFields
     */
    private Long initializeFieldsOfClass(Object obj, Class<?> clazz, Long id) {

        Field idField = null;
        for (Field field : clazz.getDeclaredFields()) {

            idField = getIdComponentField(field, idField);

            Inject inject = field.getAnnotation(Inject.class);
            if (inject == null) {
                continue;
            }

            try {
                initializeInjectedField(field, obj);
            } catch (IllegalArgumentException | IllegalAccessException | InjectionException ex) {
                if (inject.required()) {
                    throw new InjectionException("Failed to initialize field: " + field.getName() + ", "+ ex.getMessage());
                }
            }
        }

        if (idField == null) {
            throw new InjectionException("@ComponentId is not present in " + clazz.getName());
        }

        if (id == null) {
            id = idGenerator.getAndIncrement();
        }
        try {
            idField.setAccessible(true);
            idField.set(obj, id);
        } catch (IllegalAccessException e) {
            throw new InjectionException("Id field could not be set");
        }

        return id;
    }

    /**
     *
     * @param field - should be annotated with @Inject
     */
    private void initializeInjectedField(Field field, Object object) throws IllegalAccessException {
        Inject inject = field.getAnnotation(Inject.class);
        if (inject == null) {
            return;
        }
        Class<?> fieldClass = inject.specificType() != Void.class ? inject.specificType() : field.getType();

        Component component = fieldClass.getAnnotation(Component.class);
        if (component == null) {
            throw new InjectionException("Annotated field is not component: " + field.getName());
        }

        Object newFieldObject = null;
        if ((newFieldObject = singletonMap.get(fieldClass)) == null) {

            try {
                try {
                    newFieldObject = fieldClass.getConstructor(Inject.class).newInstance(inject);
                } catch (NoSuchMethodException | InvocationTargetException e) {
                    newFieldObject = fieldClass.newInstance();
                }
            } catch (InstantiationException ex) {
                throw new InjectionException("Could not instatiate new object");
            }


            if (component.scope().equals(ScopeType.SINGLETON)) {
                singletonMap.put(fieldClass, newFieldObject);
            }

            initializeFields(newFieldObject);
        }

        field.setAccessible(true);
        field.set(object, newFieldObject);
    }

    /**
     * Sets id to Field if is annotated with ComponentId.
     *
     */
    private Field getIdComponentField(Field field, Field idFiled) throws InjectionException {
        ComponentId componentId = field.getAnnotation(ComponentId.class);
        if (componentId != null) {
            if (field.getType().equals(Long.class) == false) {
                throw new InjectionException(field.getName() + " has to be Long");
            }

            return field;
        }

        return idFiled;
    }
}

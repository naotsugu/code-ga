package com.etc9.ga;

import javax.inject.Inject;
import java.lang.reflect.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.etc9.ga.Reflections.*;

/**
 * Instance builder that
 * Construct instance and inject filed and method.
 *
 * @author Naotsugu Kobayashi
 */
public class InstanceBuilder {

    /** Context of injection. */
    private InjectionContext context;


    /**
     * Constructor.
     * @param context context
     */
    public InstanceBuilder(InjectionContext context) {
        this.context = context;
    }


    /**
     * Create new instance.
     * @param implType implement type
     * @param <T> type of creation
     * @return new instance
     */
    public <T> Object newInstance(Class<T> implType) {

        T instance = implType.cast(context.fromCache(implType, construct()));

        injectFieldAndMethod(implType, instance);

        return instance;
    }

    /**
     * Construct object instance function.
     * @return function
     */
    private Function<Class<?>, Object> construct() {
        return t -> {
            Constructor<?> constructor = getInjectableConstructors(t)
                    .map(accessible())
                    .findFirst()
                    .orElseThrow(RuntimeException::new);

            Object[] constructorArgs = Stream.of(constructor.getParameters())
                    .map(param -> (Object) context.mapOf(InjectionPoint.of(param)).get())
                    .toArray(Object[]::new);

            return t.cast(create(constructor, constructorArgs));
        };
    }

    /**
     * Inject field and method.
     * @param implType implement type
     * @param object instance of type
     * @param <T> type
     */
    private <T> void injectFieldAndMethod(Class<T> implType, T object) {

        Map<Class<? super T>, List<Method>> map = Reflections.getMethodsUnOverridden(implType, null);

        fromRootStream(implType).forEach(c -> {
            Stream.of(c.getDeclaredFields())
                    .filter(field -> field.isAnnotationPresent(Inject.class))
                    .filter(field -> !Modifier.isFinal(field.getModifiers()))
                    .filter(field -> !Modifier.isStatic(field.getModifiers()))
                    .map(accessible())
                    .forEach(field -> fieldSet(field, object,
                            context.mapOf(InjectionPoint.of(field)).get()));

            List<Method> methods = map.containsKey(c) ? map.get(c) : Collections.emptyList();
            methods.stream()
                    .filter(method -> method.isAnnotationPresent(Inject.class))
                    .map(accessible())
                    .forEach(method -> methodSet(method, object, paramInstance(method)));
        });
    }


    /**
     * Create instance of method parameter.
     * @param method method
     * @return Created instance of method parameter
     */
    private Object[] paramInstance(Method method) {
        return Stream.of(method.getParameters())
                .map(param -> (Object) context.mapOf(InjectionPoint.of(param)).get())
                .toArray(Object[]::new);
    }

}

package com.etc9.ga;

import javax.inject.Inject;
import java.lang.reflect.*;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Utility of reflection operation.
 *
 * @author Naotsugu Kobayashi
 */
public abstract class Reflections {


    /**
     * Gets injectable constructors.
     * @param type a checking class
     * @return injectable constructors
     */
    public static Stream<Constructor<?>> getInjectableConstructors(Class<?> type) {
        return Stream.of(type.getDeclaredConstructors())
                .filter(constructor -> constructor.isAnnotationPresent(Inject.class) || constructor.getParameterCount() == 0)
                .sorted((e1, e2) -> e1.isAnnotationPresent(Inject.class) ? 1 : 0);
    }


    /**
     * Stream of class hierarchy.
     * @param type leaf class type
     * @param <T> class of type
     * @return Stream of class hierarchy
     */
    public static <T> Stream<Class<? super T>> fromRootStream(Class<T> type) {
        List<Class<? super T>> list = new ArrayList<>();
        for (Class<? super T> c = type; c != Object.class; c = c.getSuperclass()) {
            list.add(c);
        }
        Collections.reverse(list);
        return list.stream();
    }


    /**
     * Sets this accessible object to be accessible.
     */
    public static <T extends AccessibleObject> Function<T, T> accessible() {
        return accessibleObject -> {
            if (accessibleObject.isAccessible()) return accessibleObject;

            AccessController.doPrivileged((PrivilegedAction<Object>) () -> {
                accessibleObject.setAccessible(true);
                return null;
            });
            return accessibleObject;
        };
    }


    /**
     * Construct instance from constructor with argument.
     *
     * @param constructor constructor of target class
     * @param parameters array of objects to be passed as arguments to the constructor call
     * @param <T> type of instance
     * @return instance of type T
     * @throws RuntimeException reflective operation exception
     */
    public static <T> T create(Constructor<T> constructor, Object...parameters) {
        try {
            return constructor.newInstance(parameters);
        }
        catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }

    }


    /**
     * Gets all methods, exclude overridden method.
     *
     * @param clazz declaring class
     * @param superClass super class of {@code clazz}
     * @param <T> type of class
     * @return class and method exclude overridden
     */
    @SuppressWarnings("unchecked")
    public static <T> Map<Class<? super T>, List<Method>> getMethodsUnOverridden(final Class<T> clazz, final Class<? super T> superClass) {

        final Class<?> declaringClass = superClass == null ? clazz : superClass;

        if (declaringClass.equals(Object.class)) {
            return Collections.emptyMap();
        }

        Map<Class<? super T>, List<Method>> map = new HashMap<>();

        map.putAll(Stream.of(declaringClass.getDeclaredMethods())
                        .filter(m -> isNotOverridden(m, clazz))
                        .collect(Collectors.groupingBy(m -> (Class<? super T>) m.getDeclaringClass())));

        map.putAll(Reflections.getMethodsUnOverridden(clazz, (Class<? super T>) declaringClass.getSuperclass()));

        return map;
    }


    /**
     * Return {@code true} if method is not override in sub class.
     *
     * @param method a checking method
     * @param subClass a leaf of class hierarchy that we are checking
     * @return Return {@code true} if method is not override in
     * sub class, {@code false} otherwise.
     */
    private static boolean isNotOverridden(final Method method, final Class<?> subClass) {

        final int mod = method.getModifiers();
        if (Modifier.isFinal(mod)) return true;
        if (Modifier.isPrivate(mod)) return true;
        if (isPackagePrivate(method) &&
                !method.getDeclaringClass().getPackage().equals(subClass.getPackage())) {
            return true;
        }

        try {
            Method methodOfSub = subClass.getDeclaredMethod(method.getName(), method.getParameterTypes());
            return methodOfSub.getDeclaringClass().equals(method.getDeclaringClass());
        } catch(NoSuchMethodException ignored) { }
        return true;
    }


    /**
     * Return {@code true} if the method argument modifier is package
     * private, {@code false} otherwise.
     *
     * @param method a checking of modifiers
     * @return {@code true} if {@code method} has package private
     * modifier, {@code false} otherwise.
     */
    private static boolean isPackagePrivate(Method method) {
        return (method.getModifiers() & (Modifier.PUBLIC | Modifier.PROTECTED | Modifier.PRIVATE)) == 0;
    }


    /**
     * Sets the field on the specified object argument to the specified
     * new value.
     *
     * @param field specified field object
     * @param target the object whose field should be modified
     * @param value the new value for the field of {@code obj} being modified
     * @return the object whose field set new value
     */
    public static Object fieldSet(Field field, Object target, Object value) {
        try {
            field.set(target, value);
            return target;
        }
        catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Invoke the method on the specified object argument to the specified
     * value.
     *
     * @param method specified method object
     * @param target the object whose method should be invoked
     * @param values the arguments of method call
     * @return the object whose method invoked
     */
    public static Object methodSet(Method method, Object target, Object...values) {
        try {
            method.invoke(target, values);
            return target;
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}

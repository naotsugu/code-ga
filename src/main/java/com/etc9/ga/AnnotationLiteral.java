package com.etc9.ga;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Arrays;
import java.util.stream.Stream;

/**
 *  Represents a Annotation type.
 *
 *  <pre>{@code
 *   AnnotationLiteral<Qualifier> literal = new AnnotationLiteral<Qualifier>(){};
 *  }</pre>
 *
 *  <pre>{@code
 *   class QualifierLiteral extends AnnotationLiteral<Qualifier> implements Qualifier {}
 *   AnnotationLiteral<Qualifier> literal = new QualifierLiteral();
 *  }</pre>
 *
 * @param <T>
 *
 * @author Naotsugu Kobayashi
 */
public abstract class AnnotationLiteral<T extends Annotation> implements Annotation, Serializable {

    /** annotation type. */
    private final transient Class<T> annotationType;

    /** annotation members. */
    private transient Method[] members;


    /**
     * Protected constructor for sub classing.
     */
    protected AnnotationLiteral() {
        Class<?> annotationLiteralSubclass = getOwnSubclass(getClass());
        annotationType = getTypeParameterOfT(annotationLiteralSubclass);
    }

    /**
     * Check sub classing.
     */
    public AnnotationLiteral<T> useStrict() {
        Stream.of(getClass().getInterfaces())
                .filter(Class::isAnnotation)
                .findAny().orElseThrow(IllegalStateException::new);
        return this;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return annotationType;
    }

    /**
     * Gets the raw type represented by this object.
     *
     * @return the raw type represented by this object
     */
    @SuppressWarnings("unchecked")
    private Class<T> getTypeParameterOfT(Class clazz) {
        return (Class<T>) getTypeParameter(clazz);
    }


    /**
     * Return the direct child class of {@code AnnotationLiteral}.
     * Recursive find with {@code getSuperclass()}.
     *
     * @param clazz processed class
     */
    private static Class<?> getOwnSubclass(Class<?> clazz) {

        Class<?> superClass = clazz.getSuperclass();

        if (superClass.equals(AnnotationLiteral.class)) {
            // find out direct child
            return clazz;
        }

        if (superClass.equals(Object.class)) {
            throw new RuntimeException("Not extended TypeLiteral. [" + clazz.getSimpleName() + "]");
        }

        return getOwnSubclass(superClass);
    }


    /**
     * Return the type parameter of {@code AnnotationLiteral<T>}.
     *
     * @param subclass subclass of AnnotationLiteral<T> to analyze
     * @return the parametrized type
     */
    private static Type getTypeParameter(Class<?> subclass) {

        Type type = subclass.getGenericSuperclass();

        if (type instanceof ParameterizedType) {
            return ((ParameterizedType) type).getActualTypeArguments()[0];
        }

        throw new RuntimeException("Missing type parameter.");
    }


    /**
     * Gets members.
     * @return members
     */
    private Method[] getMembers() {
        if (members == null) {
            members = AccessController.doPrivileged(
                    (PrivilegedAction<Method[]>) () -> annotationType().getDeclaredMethods());

            if ( members.length > 0 && !annotationType().isAssignableFrom(this.getClass()) ) {
                throw new RuntimeException(
                        String.format("%s does not implement the annotation type with members %s",
                                getClass(), annotationType().getName()));
            }
        }
        return members;
    }



    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Annotation)) {
            return false;
        }
        Annotation that = (Annotation) other;
        if (this.annotationType().equals(that.annotationType())) {
            for (Method member : getMembers()) {
                Object thisValue = invoke(member, this);
                Object thatValue = invoke(member, that);
                if (thisValue instanceof byte[] && thatValue instanceof byte[]) {
                    if ( !Arrays.equals((byte[]) thisValue, (byte[]) thatValue) ) return false;
                }
                else if (thisValue instanceof short[] && thatValue instanceof short[]) {
                    if ( !Arrays.equals((short[])thisValue, (short[])thatValue) ) return false;
                }
                else if (thisValue instanceof int[] && thatValue instanceof int[]) {
                    if ( !Arrays.equals((int[])thisValue, (int[])thatValue) ) return false;
                }
                else if (thisValue instanceof long[] && thatValue instanceof long[]) {
                    if ( !Arrays.equals((long[])thisValue, (long[])thatValue) ) return false;
                }
                else if (thisValue instanceof float[] && thatValue instanceof float[]) {
                    if ( !Arrays.equals((float[])thisValue, (float[])thatValue) ) return false;
                }
                else if (thisValue instanceof double[] && thatValue instanceof double[]) {
                    if ( !Arrays.equals((double[])thisValue, (double[])thatValue) ) return false;
                }
                else if (thisValue instanceof char[] && thatValue instanceof char[]) {
                    if ( !Arrays.equals((char[])thisValue, (char[])thatValue) ) return false;
                }
                else if (thisValue instanceof boolean[] && thatValue instanceof boolean[]) {
                    if ( !Arrays.equals((boolean[])thisValue, (boolean[])thatValue) ) return false;
                }
                else if (thisValue instanceof Object[] && thatValue instanceof Object[]) {
                    if ( !Arrays.equals((Object[])thisValue, (Object[])thatValue) ) return false;
                }
                else {
                    if (!thisValue.equals(thatValue)) return false;
                }
            }
            return true;
        }
        return false;
    }


    @Override
    public int hashCode() {

        int hashCode = annotationType().hashCode();
        for (Method member: getMembers()) {
            int memberNameHashCode = 127 * member.getName().hashCode();
            Object value = invoke(member, this);
            int memberValueHashCode;
            if (value instanceof boolean[]) {
                memberValueHashCode = Arrays.hashCode((boolean[]) value);
            }
            else if (value instanceof short[]) {
                memberValueHashCode = Arrays.hashCode((short[]) value);
            }
            else if (value instanceof int[]) {
                memberValueHashCode = Arrays.hashCode((int[]) value);
            }
            else if (value instanceof long[]) {
                memberValueHashCode = Arrays.hashCode((long[]) value);
            }
            else if (value instanceof float[]) {
                memberValueHashCode = Arrays.hashCode((float[]) value);
            }
            else if (value instanceof double[]) {
                memberValueHashCode = Arrays.hashCode((double[]) value);
            }
            else if (value instanceof byte[]) {
                memberValueHashCode = Arrays.hashCode((byte[]) value);
            }
            else if (value instanceof char[]) {
                memberValueHashCode = Arrays.hashCode((char[]) value);
            }
            else if (value instanceof Object[]) {
                memberValueHashCode = Arrays.hashCode((Object[]) value);
            }
            else {
                memberValueHashCode = value.hashCode();
            }
            hashCode += memberNameHashCode ^ memberValueHashCode;
        }
        return hashCode;
    }


    private static Object invoke(Method method, Object instance) {
        try {
            Reflections.accessible().apply(method);
            return method.invoke(instance);
        }
        catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(
                    String.format("Error checking value of member method %s on %s",
                            method.getName(), method.getDeclaringClass()), e);

        }
    }

}

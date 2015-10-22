package com.etc9.ga;

import java.lang.reflect.*;
import java.util.Objects;

/**
 *  Represents a generic type {@code T}.
 *
 *  Represents any parameterized type may be obtained by subclassing {@code TypeLiteral<T>}.
 *
 *  <pre>{@code
 *   TypeLiteral<List<String>> stringListType = new TypeLiteral<>() {};
 *  }</pre>
 *
 * @param <T>
 *
 * @author Naotsugu Kobayashi
 */
public abstract class TypeLiteral<T> {

    /** Store the actual type. */
    private final Type type;

    /** Actual raw parameter type. */
    private final Class<T> rawType;


    /**
     * Protected constructor for subclassing.
     */
    protected TypeLiteral() {
        final Class<?> subclass = getOwnSubclass(this.getClass());
        this.type = getTypeParameter(subclass);
        this.rawType = getRawTypeOfT(type);
    }

    /**
     * Private constructor.
     * @param type type (e.g., List<String>)
     * @param rawType rawType (e.g., List)
     */
    private TypeLiteral(Type type, Class<T> rawType) {
        this.type = type;
        this.rawType = rawType;
    }


    /**
     * Gets the actual type represented by this object.
    s * @return the actual type represented by this object
     */
    public Type getType() {
        return type;
    }

    /**
     * Gets the raw type represented by this object.
     * @return the raw type represented by this object
     */
    public Class<T> getRawType() {
        return rawType;
    }


    /**
     * Gets the types associated with this literal.
     *
     * @return A non-null (but possibly empty) array of types associated with this literal
     */
    public final Type[] getParameterTypes() {
        if (type instanceof ParameterizedType) {
            return ((ParameterizedType) type).getActualTypeArguments();
        }
        return new Type[0];
    }


    /**
     * Gets the raw type represented by this object.
     *
     * @return the raw type represented by this object
     */
    @SuppressWarnings("unchecked")
    private static <T> Class<T> getRawTypeOfT(Type type) {
        return (Class<T>) getRawType(type);
    }


    /**
     * Gets the base associated class from parameter.
     *
     * @param type type to analyze
     * @return The base class for the type, or null if there is none (e.g., Wildcard)
     */
    private static Class<?> getRawType(Type type) {

        Objects.requireNonNull(type);

        if (type instanceof Class) {
            return (Class<?>) type;
        }

        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            return (Class<?>) parameterizedType.getRawType();
        }

        if (type instanceof GenericArrayType) {
            return Object[].class;
        }

        if (type instanceof WildcardType) {
            return null;
        }

        throw new RuntimeException("Illegal type.");
    }


    /**
     * Return the direct child class of {@code TypeLiteral}.
     * Recursive find with {@code getSuperclass()}.
     *
     * @param clazz processed class
     */
    private static Class<?> getOwnSubclass(Class<?> clazz) {

        Class<?> superClass = clazz.getSuperclass();

        if (superClass.equals(TypeLiteral.class)) {
            // find out direct child
            return clazz;
        }

        if (superClass.equals(Object.class)) {
            throw new RuntimeException("Not extended TypeLiteral. [" + clazz.getSimpleName() + "]");
        }

        return getOwnSubclass(superClass);
    }


    /**
     * Return the type parameter of {@code TypeLiteral<T>}.
     *
     * @param subclass subclass of TypeLiteral<T> to analyze
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
     * Construct {@code TypeLiteral<T>}.
     * @param clazz type
     * @param <T> type of class
     * @return TypeLiteral
     */
    public static <T> TypeLiteral<T> of(Class<T> clazz) {
        return new SimpleTypeLiteral<>(clazz, clazz);
    }

    /**
     * Construct {@code TypeLiteral<T>}
     * @param type type
     * @param <T> type of literal
     * @return TypeLiteral
     */
    public static <T> TypeLiteral<T> of(Type type) {
        return new SimpleTypeLiteral<>(type, getRawTypeOfT(type));
    }


    /**
     * Simple implementation class of {@code TypeLiteral<T>}.
     * @param <T> type
     */
    private static final class SimpleTypeLiteral<T> extends TypeLiteral<T> {
        SimpleTypeLiteral(Type type, Class<T> clazz) {
            super(type, clazz);
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !TypeLiteral.class.isAssignableFrom(o.getClass())) return false;

        TypeLiteral that = (TypeLiteral) o;

        if (rawType != null ? !rawType.equals(that.rawType) : that.rawType != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (rawType != null ? rawType.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "TypeLiteral{" +
                "type=" + type +
                ", rawType=" + rawType +
                '}';
    }
}

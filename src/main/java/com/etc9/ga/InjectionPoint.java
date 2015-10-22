package com.etc9.ga;

import javax.inject.Qualifier;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Represents the point of injection.
 * injectee field or one of method parameters
 *
 * @param <T> type of injection point
 *
 * @author Naotsugu Kobayashi
 */
public class InjectionPoint<T> {

    /** type literal. */
    private final TypeLiteral<T> typeLiteral;

    /** set of qualifier annotation. */
    private final Set<Annotation> qualifiers;


    /**
     * Constructor.
     * @param typeLiteral type literal
     * @param qualifiers qualifier annotations
     */
    public InjectionPoint(TypeLiteral<T> typeLiteral, Annotation... qualifiers) {
        this.typeLiteral = Objects.requireNonNull(typeLiteral);
        this.qualifiers = Stream.of(qualifiers)
                .filter(q -> q.annotationType().isAnnotationPresent(Qualifier.class))
                .collect(Collectors.toSet());
    }

    /**
     * Static factory of {@code InjectionPoint}.
     * @param point class of injection point
     * @param qualifiers qualifier annotations
     * @param <T> type
     * @return {@code InjectionPoint}
     */
    public static <T> InjectionPoint<T> of(Class<T> point, Annotation... qualifiers) {
        return new InjectionPoint<>(TypeLiteral.of(point), qualifiers);
    }

    /**
     * Static factory of {@code InjectionPoint}.
     * @param parameter Parameter
     * @param <T> type
     * @return {@code InjectionPoint}
     */
    public static <T> InjectionPoint<T> of(Parameter parameter) {
        return new InjectionPoint<T>(
                TypeLiteral.of(parameter.getParameterizedType()),
                parameter.getDeclaredAnnotations());
    }

    /**
     * Static factory of {@code InjectionPoint}.
     * @param field field
     * @param <T> type
     * @return {@code InjectionPoint}
     */
    public static <T> InjectionPoint<T> of(Field field) {
        return new InjectionPoint<T>(
                TypeLiteral.of(field.getGenericType()),
                field.getDeclaredAnnotations());
    }


    /**
     * Gets Qualifiers
     * @return qualifiers
     */
    Set<Annotation> getQualifiers() {
        return qualifiers;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InjectionPoint that = (InjectionPoint) o;

        if (qualifiers != null ? !qualifiers.equals(that.qualifiers) : that.qualifiers != null) return false;
        if (typeLiteral != null ? !typeLiteral.equals(that.typeLiteral) : that.typeLiteral != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = typeLiteral != null ? typeLiteral.hashCode() : 0;
        result = 31 * result + (qualifiers != null ? qualifiers.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "InjectionPoint{" +
                "typeLiteral=" + typeLiteral +
                ", qualifiers=" + qualifiers +
                '}';
    }
}

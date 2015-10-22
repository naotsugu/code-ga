package com.etc9.ga;

import javax.inject.Provider;
import javax.inject.Singleton;
import java.lang.annotation.Annotation;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Injection context.
 *
 * @author Naotsugu Kobayashi
 */
public class InjectionContext {

    /** Mapping mapping. */
    private final InjectionMapping mapping = new InjectionMapping();

    /** Cache of instance. */
    private final ConcurrentMap<Class<?>, Object> instanceCache = new ConcurrentHashMap<>();


    /**
     * Start to create new injection rule.
     * @param pointClass point of injection
     * @param annotations qualifiers
     * @param <T> type
     * @return rule builder
     */
    public <T> InjectionRuleBuilder<T> ruleOf(Class<T> pointClass, Annotation...annotations) {
        return new InjectionRuleBuilder<>(this, pointClass, annotations);
    }


    /**
     * Start to create new injection rule of provider.
     * @param typeLiteral point of injection
     * @param annotations qualifiers
     * @param <T> target type of provider
     * @return rule builder
     */
    public <T> ProviderRuleBuilder<T> ruleOf(TypeLiteral<Provider<T>> typeLiteral, Annotation... annotations) {
        return new ProviderRuleBuilder<>(this, typeLiteral, annotations);
    }

    /**
     * Gets mapped provider from mapping.
     * @param point injection point
     * @param <T> type
     * @return mapped provider
     */
    @SuppressWarnings("unchecked")
    public <T> Supplier<? extends T> mapOf(InjectionPoint<T> point) {

        if (!mapping.hasMappingOf(point)) {
            throw new RuntimeException("Undefined mapping. [" + point + "]");
        }

        return (Supplier<? extends T>) mapping.get(point);
    }

    /**
     * add mapping rule.
     * @param point injection point
     * @param supplier concrete supplier
     * @param <T> type of injection
     */
    <T> void add(InjectionPoint<T> point, Supplier<? extends T> supplier) {
        mapping.put(point, supplier);
    }


    /**
     * Gets instance from cache if a class marked singleton.
     * @param type type
     * @param mappingFunction function of create instance
     * @return instance
     */
    Object fromCache(Class<?> type, Function<Class<?>, Object> mappingFunction) {
        if (!type.isAnnotationPresent(Singleton.class)) {
            return mappingFunction.apply(type);
        }
        Object obj = instanceCache.computeIfAbsent(type, mappingFunction);
        return obj == null ? instanceCache.get(type) : obj;
    }


}

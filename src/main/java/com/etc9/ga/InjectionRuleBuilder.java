package com.etc9.ga;

import java.lang.annotation.Annotation;
import java.util.function.Supplier;

/**
 * Builder of injection mapping rule.
 * @param <T> type
 * @author Naotsugu kobayashi
 */
public class InjectionRuleBuilder<T> {

    /** context. */
    private final InjectionContext context;
    /** typeLiteral. */
    private final TypeLiteral<T> typeLiteral;
    /** annotations. */
    private final Annotation[] annotations;
    /** mappedClass. */
    private Class<? extends T> mappedClass;

    /**
     * Constructor.
     * @param context context
     * @param pointClass injection point class
     * @param annotations qualifiers
     */
    InjectionRuleBuilder(InjectionContext context, Class<T> pointClass, Annotation...annotations) {
        this.context = context;
        this.annotations = annotations;
        this.typeLiteral = TypeLiteral.of(pointClass);
    }


    /**
     * Build injection rule with specification mapping class.
     * @param mappedClass mapped class
     */
    public void map(Class<? extends T> mappedClass) {
        this.mappedClass = mappedClass;
        context.add(new InjectionPoint<>(typeLiteral, annotations), supplier());
    }

    /**
     * Create provider.
     * @return provider
     */
    private Supplier<? extends T> supplier() {
        return () -> mappedClass.cast(
                new InstanceBuilder(context).newInstance(mappedClass));
    }

}

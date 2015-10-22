package com.etc9.ga;

import javax.inject.Provider;
import java.util.function.Supplier;

/**
 * Implementation of {@code injector}.
 *
 * @author Naotsugu Kobayashi
 */
public class InjectorImpl implements Injector {

    /** context of injection. */
    private final InjectionContext context;


    /**
     * Construct injector.
     * @param context context
     */
    public InjectorImpl(InjectionContext context) {
        this.context = context;
    }


    @Override
    public <T> T getInstance(Class<T> clazz) {
        Supplier<?> supplier = context.mapOf(InjectionPoint.of(clazz));
        return clazz.cast(supplier.get());
    }

}

package com.etc9.ga;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

/**
 * Injection mapping.
 * Provide rule of injection.
 *
 * @author Naotsugu Kobayashi
 */
public class InjectionMapping {

    /** Mapping rules. */
    private final ConcurrentMap<InjectionPoint<?>, Supplier<?>> rules = new ConcurrentHashMap<>();

    /**
     * Puts injection rule mapping.
     * @param point injection point
     * @param supplier supplier for injection point
     */
    public void put(InjectionPoint<?> point, Supplier<?> supplier) {
        rules.put(point, supplier);
    }

    /**
     * Gets Supplier for injection point.
     * @param point injection point
     * @return supplier for injection point
     */
    public Supplier<?> get(InjectionPoint<?> point) {
        return rules.get(point);
    }


    /**
     * Returns {@code true} if contains a mapping for the specified
     * injection point, otherwise {@code false}.
     * @param point injection point
     * @return returns {@code true} if contains a mapping
     */
    public boolean hasMappingOf(InjectionPoint<?> point) {
        return rules.containsKey(point);
    }


}

package com.etc9.ga;

/**
 * Builds the graphs of objects that make up your application.
 * @author Naotsugu Kobayashi
 */
public interface Injector {

    /**
     * Returns the appropriate instance for the given injection type.
     *
     * @param type a injection type
     * @param <T> instance type
     * @return created instance
     */
    <T> T getInstance(Class<T> type);

}

package com.etc9.core.matcher;

import org.hamcrest.Matcher;

import java.lang.reflect.Type;

import static org.hamcrest.CoreMatchers.is;

/**
 * A set of custom matcher.
 *
 * @author Naotsugu Kobayashi
 */
public abstract class Matchers {

    /**
     * Matcher of Same class.
     * @param clazz target of expected
     * @return Matcher
     */
    public static Matcher<Class<?>> isClassOf(Class<?> clazz) {
        @SuppressWarnings("unchecked")
        Matcher<Class<?>> result = (Matcher<Class<?>>)(Matcher<?>)is((Object)clazz);
        return result;
    }

}

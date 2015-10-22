package com.etc9.ga;

import org.junit.Test;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import static com.etc9.core.matcher.Matchers.isClassOf;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Test of {@link TypeLiteral}.
 *
 * @author Naotsugu Kobayashi
 */
public class TypeLiteralTest {

    /**
     * Tests the type return from a TypeLiteral.
     */
    @Test public void testTypeLiteral() throws Exception {

        TypeLiteral<List<String>> stringListType = new TypeLiteral<List<String>>(){};

        assertThat(stringListType.getType(), instanceOf(ParameterizedType.class));
        assertThat(stringListType.getRawType(), isClassOf(List.class));

        ParameterizedType pt = (ParameterizedType) stringListType.getType();
        assertThat(pt.getActualTypeArguments().length, is(1));
        assertThat((Class<?>)pt.getActualTypeArguments()[0], isClassOf(String.class));

        assertThat(stringListType.getRawType(), isClassOf(List.class));
    }


}

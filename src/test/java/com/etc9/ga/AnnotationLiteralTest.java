package com.etc9.ga;

import org.atinject.tck.auto.Drivers;
import org.junit.Test;

import javax.inject.Named;
import javax.inject.Qualifier;

import java.lang.annotation.Annotation;

import static com.etc9.core.matcher.Matchers.isClassOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Test of {@link AnnotationLiteral}.
 *
 * @author Naotsugu Kobayashi
 */
public class AnnotationLiteralTest {

    @Drivers Object drivers;
    @Named("spare") Object spare;

    private static Annotation qualifier(String name) throws NoSuchFieldException {
        return AnnotationLiteralTest.class.getDeclaredField(name).getAnnotations()[0];
    }


    @Test
    public void testAnnotationLiteral1() throws Exception {

        class QualifierLiteral extends AnnotationLiteral<Qualifier> implements Qualifier {}
        AnnotationLiteral<Qualifier> literal = new QualifierLiteral();

        assertThat(literal.annotationType(), isClassOf(Qualifier.class));

    }


    @Test
    public void testAnnotationLiteral2() throws Exception {

        AnnotationLiteral<Qualifier> literal = new AnnotationLiteral<Qualifier>(){};

        assertThat(literal.annotationType(), isClassOf(Qualifier.class));

    }


    @Test
    public void testAnnotationLiteral3() throws Exception {

        class DriversLiteral extends AnnotationLiteral<Drivers> implements Drivers {}
        AnnotationLiteral<Drivers> literal = new DriversLiteral().useStrict();

        assertThat(literal.equals(qualifier("drivers")), is(true));


    }


    @Test
    public void testAnnotationLiteral4() throws Exception {

        AnnotationLiteral<Drivers> literal = new AnnotationLiteral<Drivers>(){};

        assertThat(literal.equals(qualifier("drivers")), is(true));
    }

}

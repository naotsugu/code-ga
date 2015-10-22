package com.etc9.ga;

import org.atinject.tck.auto.Drivers;
import org.junit.Test;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Qualifier;
import java.util.List;

import static com.etc9.core.matcher.Matchers.isClassOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Test of {@link InjectionPoint}.
 *
 * @author Naotsugu Kobayashi
 */
public class InjectionPointTest {


    @Test
    public void testOf() throws Exception {
        InjectionPoint point = InjectionPoint.of(Number.class);
    }

    @Test
    public void testOf1() throws Exception {

        class DriversLiteral extends AnnotationLiteral<Drivers> implements Drivers {}
        AnnotationLiteral<Drivers> driversLiteral = new DriversLiteral().useStrict();
        AnnotationLiteral<Inject> injectLiteral = new AnnotationLiteral<Inject>(){};

        InjectionPoint point = InjectionPoint.of(Number.class, driversLiteral, injectLiteral);

        assertThat(point.getQualifiers().stream().findFirst().get(), is(driversLiteral));
    }

}

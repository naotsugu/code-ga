package com.etc9.ga;

import org.atinject.tck.auto.FuelTank;
import org.junit.Test;

/**
 * A test of {@link InjectorImpl}.
 *
 * @author Naotsugu Kobayashi
 */
public class InjectorImplTest {

    @Test
    public void testGetInstance() throws Exception {
        InjectionContext context = new InjectionContext();
        context.ruleOf(FuelTank.class).map(FuelTank.class);

        Injector injector = new InjectorImpl(context);
        injector.getInstance(FuelTank.class);
    }
}

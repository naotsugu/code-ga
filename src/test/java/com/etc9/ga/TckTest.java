package com.etc9.ga;

import junit.framework.Test;
import org.atinject.tck.Tck;
import org.atinject.tck.auto.*;
import org.atinject.tck.auto.accessories.Cupholder;
import org.atinject.tck.auto.accessories.SpareTire;

import static com.etc9.ga.support.Tcks.*;

/**
 * TCK test class.
 *
 *
 * TCK class hierarchy is ..
 * <pre>
 * Car (interface)
 *     Convertible (class)
 *
 * Seat (Singleton class)
 *    DriversSeat (class)
 *
 * Engine (abstract class)
 *     GasEngine (abstract class)
 *         V8Engine (class)
 * RoundThing (class)
 *     Tire  (class)
 *         SpareTire  (class)
 *
 * FuelTank  (class)
 *
 * Seatbelt  (class)
 *
 * Cupholder (Singleton class)
 * </pre>
 *
 * @author Naotsugu Kobayashi
 */
public class TckTest {

    public static Test suite() throws Exception {

        InjectionContext context = new InjectionContext();

        context.ruleOf(Car.class).map(Convertible.class);
        context.ruleOf(Seat.class).map(Seat.class);
        context.ruleOf(Seat.class, driversAnn).map(DriversSeat.class);
        context.ruleOf(DriversSeat.class).map(DriversSeat.class);
        context.ruleOf(Tire.class).map(Tire.class);
        context.ruleOf(Tire.class, spareAnn).map(SpareTire.class);
        context.ruleOf(Engine.class).map(V8Engine.class);
        context.ruleOf(V8Engine.class).map(V8Engine.class);
        context.ruleOf(Cupholder.class).map(Cupholder.class);
        context.ruleOf(SpareTire.class).map(SpareTire.class);
        context.ruleOf(FuelTank.class).map(FuelTank.class);

        context.ruleOf(seatProvider).map(SeatProvider.class);
        context.ruleOf(driversSeatProvider, driversAnn).map(DriversSeatProvider.class);
        context.ruleOf(tireProvider).map(TireProvider.class);
        context.ruleOf(spareTireProvider, spareAnn).map(SpareTireProvider.class);
        context.ruleOf(engineProvider).map(EngineProvider.class);

        Injector injector = new InjectorImpl(context);

        Car car = injector.getInstance(Car.class);

        final boolean supportsStatic = false;
        final boolean supportsPrivate = true;
        return Tck.testsFor(car, supportsStatic, supportsPrivate);
    }

}

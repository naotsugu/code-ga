package com.etc9.ga.support;

import com.etc9.ga.TypeLiteral;
import org.atinject.tck.auto.*;
import org.atinject.tck.auto.accessories.SpareTire;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import java.lang.annotation.Annotation;

/**
 * Utility of TCK.
 * @author Naotsugu Kobayashi
 */
public abstract class Tcks {

    @Drivers Object drivers;
    @Named("spare") Object spare;

    public static Annotation driversAnn;
    public static Annotation spareAnn;
    static {
        try {
            driversAnn = Tcks.class.getDeclaredField("drivers").getAnnotations()[0];
            spareAnn = Tcks.class.getDeclaredField("spare").getAnnotations()[0];
        } catch (NoSuchFieldException ignore) { }
    }

    public static TypeLiteral<Provider<Seat>> seatProvider = new TypeLiteral<Provider<Seat>>(){};
    public static TypeLiteral<Provider<Seat>> driversSeatProvider = new TypeLiteral<Provider<Seat>>(){};
    public static TypeLiteral<Provider<Tire>> tireProvider = new TypeLiteral<Provider<Tire>>(){};
    public static TypeLiteral<Provider<Tire>> spareTireProvider = new TypeLiteral<Provider<Tire>>(){};
    public static TypeLiteral<Provider<Engine>> engineProvider = new TypeLiteral<Provider<Engine>>(){};


    public static class TireProvider implements Provider<Tire> {
        @Inject Tire tire;
        @Override public Tire get() { return tire; }
    }
    public static class SpareTireProvider implements Provider<Tire> {
        @Inject SpareTire spareTire;
        @Override public Tire get() { return spareTire; }
    }

    public static class SeatProvider implements Provider<Seat> {
        @Inject Seat seat;
        @Override public Seat get() { return seat; }
    }

    public static class DriversSeatProvider implements Provider<Seat> {
        @Inject DriversSeat driversSeat;
        @Override public Seat get() { return driversSeat; }
    }

    public static class EngineProvider implements Provider<Engine> {
        @Inject V8Engine v8Engine;
        @Override public Engine get() { return v8Engine; }
    }
}

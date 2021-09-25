package net.lortservers.iris.wrap;

import java.util.concurrent.atomic.AtomicReference;

public class AtomicDouble extends AtomicReference<Double> {
    public AtomicDouble() {
        super();
    }

    public AtomicDouble(double def) {
        super(def);
    }

    public Double getAndIncrement(double val) {
        final Double newVal = get() + val;
        set(newVal);
        return newVal;
    }

    public Double getAndDecrement(double val) {
        final Double newVal = get() - val;
        set(newVal);
        return newVal;
    }
}

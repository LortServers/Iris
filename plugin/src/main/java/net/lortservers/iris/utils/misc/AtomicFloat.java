package net.lortservers.iris.utils.misc;

import java.util.concurrent.atomic.AtomicReference;

public class AtomicFloat extends AtomicReference<Float> {
    public AtomicFloat() {
        super();
    }

    public AtomicFloat(float def) {
        super(def);
    }

    public Float getAndIncrement(float val) {
        final Float newVal = get() + val;
        set(newVal);
        return newVal;
    }

    public Float getAndDecrement(float val) {
        final Float newVal = get() - val;
        set(newVal);
        return newVal;
    }
}

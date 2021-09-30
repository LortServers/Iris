package net.lortservers.iris.utils;

import java.util.function.Supplier;

public final class Reflections {
    public static <T> T defaultIfThrown(Supplier<T> supplier, T def) {
        try {
            return supplier.get();
        } catch (Throwable ignored) {
            return def;
        }
    }
}

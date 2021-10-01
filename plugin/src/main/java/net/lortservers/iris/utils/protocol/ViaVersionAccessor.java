package net.lortservers.iris.utils.protocol;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.screamingsandals.lib.utils.reflect.InstanceMethod;
import org.screamingsandals.lib.utils.reflect.Reflect;

import java.util.Objects;

public final class ViaVersionAccessor {
    private static final String VIA_CLASS = "com.viaversion.viaversion.api.Via";
    private static @Nullable Object VIA_API = null;

    static {
        if (Reflect.has(VIA_CLASS)) {
            VIA_API = Reflect.getMethod(VIA_CLASS, "getAPI").invokeStatic();
        }
    }

    public static boolean hasVia() {
        return VIA_API != null;
    }

    public static @Nullable Object getViaApi() {
        return VIA_API;
    }

    public static @Nullable InstanceMethod getViaApiMethod(String name, Class<?> params) {
        if (!hasVia()) return null;
        final InstanceMethod method = Reflect.getMethod(Objects.requireNonNull(VIA_API), name, params);
        return (method.getMethod() == null) ? null : method;
    }
}

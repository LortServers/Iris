package net.lortservers.iris.utils.protocol;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.screamingsandals.lib.utils.reflect.ClassMethod;
import org.screamingsandals.lib.utils.reflect.Reflect;

public final class ProtocolSupportAccessor {
    private static final String PS_CLASS = "protocolsupport.api.ProtocolSupportAPI";

    public static boolean hasProtocolSupport() {
        return Reflect.has(PS_CLASS);
    }

    public static @Nullable ClassMethod getPsApiMethod(String method, Class<?>... params) {
        final ClassMethod classMethod = Reflect.getMethod(PS_CLASS, method, params);
        return (classMethod.getMethod() == null) ? null : classMethod;
    }
}

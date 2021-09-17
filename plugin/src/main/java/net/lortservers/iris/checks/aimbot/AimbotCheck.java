package net.lortservers.iris.checks.aimbot;

import net.lortservers.iris.checks.CheckImpl;
import org.checkerframework.checker.nullness.qual.NonNull;

public abstract class AimbotCheck extends CheckImpl {
    @Override
    public @NonNull String getName() {
        return "Aimbot";
    }
}

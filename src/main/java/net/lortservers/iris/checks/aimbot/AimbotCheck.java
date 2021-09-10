package net.lortservers.iris.checks.aimbot;

import net.lortservers.iris.checks.Check;
import org.checkerframework.checker.nullness.qual.NonNull;

public abstract class AimbotCheck extends Check {
    @Override
    public @NonNull String getName() {
        return "Aimbot";
    }
}

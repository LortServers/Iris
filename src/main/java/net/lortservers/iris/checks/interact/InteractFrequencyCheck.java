package net.lortservers.iris.checks.interact;

import net.lortservers.iris.checks.Check;
import org.checkerframework.checker.nullness.qual.NonNull;

public abstract class InteractFrequencyCheck extends Check {
    @Override
    public @NonNull String getName() {
        return "InteractFrequency";
    }
}

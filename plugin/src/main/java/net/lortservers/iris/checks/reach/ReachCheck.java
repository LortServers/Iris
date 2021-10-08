package net.lortservers.iris.checks.reach;

import net.lortservers.iris.checks.CheckImpl;
import org.checkerframework.checker.nullness.qual.NonNull;

public abstract class ReachCheck extends CheckImpl {
    @Override
    public @NonNull String getName() {
        return "Reach";
    }
}

package net.lortservers.iris.checks.rigidentity;

import net.lortservers.iris.checks.CheckImpl;
import org.checkerframework.checker.nullness.qual.NonNull;

public abstract class RigidEntityCheck extends CheckImpl {
    @Override
    public @NonNull String getName() {
        return "RigidEntity";
    }
}

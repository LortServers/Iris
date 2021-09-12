package net.lortservers.iris.checks.interact.block;

import net.lortservers.iris.checks.Check;
import org.checkerframework.checker.nullness.qual.NonNull;

public abstract class BlockingFrequencyCheck extends Check {
    @Override
    public @NonNull String getName() {
        return "BlockingFrequency";
    }
}

package net.lortservers.iris.checks.interact;

import net.lortservers.iris.checks.Check;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * <p>The interact frequency check base.</p>
 */
public abstract class InteractFrequencyCheck extends Check {
    /**
     * <p>Gets the check name.</p>
     *
     * @return the check name
     */
    @Override
    public @NonNull String getName() {
        return "InteractFrequency";
    }
}

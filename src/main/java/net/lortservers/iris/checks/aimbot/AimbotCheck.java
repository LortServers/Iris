package net.lortservers.iris.checks.aimbot;

import net.lortservers.iris.checks.Check;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * <p>Aimbot check base.</p>
 */
public abstract class AimbotCheck extends Check {
    /**
     * <p>Gets the check name.</p>
     *
     * @return the check name
     */
    @Override
    public @NonNull String getName() {
        return "Aimbot";
    }
}

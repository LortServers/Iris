package net.lortservers.iris.checks.interact.block;

import net.lortservers.iris.checks.Check;
import net.lortservers.iris.checks.CheckImpl;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * <p>Blocking frequency check base.</p>
 */
public abstract class BlockingFrequencyCheck extends CheckImpl {
    /**
     * <p>Gets the check name letter.</p>
     *
     * @return the check name
     */
    @Override
    public @NonNull String getName() {
        return "BlockingFrequency";
    }
}

package net.lortservers.iris.checks.interact;

import net.lortservers.iris.api.checks.CheckAlphabet;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.screamingsandals.lib.utils.annotations.Service;

/**
 * <p>A class representing the interact frequency check type A.</p>
 */
@Service
public class InteractFrequencyCheckA extends InteractFrequencyCheck {
    /**
     * <p>Gets the check type letter.</p>
     *
     * @return the check type
     */
    @Override
    public @NonNull CheckAlphabet getType() {
        return CheckAlphabet.A;
    }
}

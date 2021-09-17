package net.lortservers.iris.checks.interact;

import net.lortservers.iris.checks.CheckAlphabet;
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

    /**
     * <p>Gets the check VL threshold.</p>
     * <p>Used for sending failed messages after the VL reaches a certain threshold.</p>
     *
     * @return the check VL threshold
     */
    @Override
    public int getVLThreshold() {
        return config().getValue("interactFrequencyAVLThreshold", Integer.class).orElse(2);
    }
}

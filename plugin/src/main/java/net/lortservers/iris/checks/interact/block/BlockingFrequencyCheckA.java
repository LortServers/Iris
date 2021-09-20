package net.lortservers.iris.checks.interact.block;

import net.lortservers.iris.checks.CheckAlphabet;
import net.lortservers.iris.managers.ConfigurationManager;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.screamingsandals.lib.utils.annotations.Service;

/**
 * <p>A class representing the blocking frequency check type A.</p>
 */
@Service
public class BlockingFrequencyCheckA extends BlockingFrequencyCheck {
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
        return ConfigurationManager.getInstance().getValue("blockingFrequencyVLThreshold", Integer.class).orElse(5);
    }
}

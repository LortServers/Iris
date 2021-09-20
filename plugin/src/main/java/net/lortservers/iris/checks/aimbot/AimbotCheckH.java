package net.lortservers.iris.checks.aimbot;

import net.lortservers.iris.checks.CheckAlphabet;
import net.lortservers.iris.managers.ConfigurationManager;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.screamingsandals.lib.utils.annotations.Service;

/**
 * <p>A class representing the aimbot check H.</p>
 */
@Service
public class AimbotCheckH extends AimbotCheck {
    /**
     * <p>Gets the check type letter.</p>
     *
     * @return the check type
     */
    @Override
    public @NonNull CheckAlphabet getType() {
        return CheckAlphabet.H;
    }

    /**
     * <p>Gets the check VL threshold.</p>
     * <p>Used for sending failed messages after the VL reaches a certain threshold.</p>
     *
     * @return the check VL threshold
     */
    @Override
    public int getVLThreshold() {
        return ConfigurationManager.getInstance().getValue("aimbotHVLThreshold", Integer.class).orElse(2);
    }
}

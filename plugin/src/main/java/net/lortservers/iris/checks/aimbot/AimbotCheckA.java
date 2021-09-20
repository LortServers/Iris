package net.lortservers.iris.checks.aimbot;

import net.lortservers.iris.checks.CheckAlphabet;
import net.lortservers.iris.managers.ConfigurationManager;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.screamingsandals.lib.utils.annotations.Service;

/**
 * <p>Class representing the aimbot check type A.</p>
 */
@Service
public class AimbotCheckA extends AimbotCheck {
    /**
     * <p>Gets the check type.</p>
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
        return ConfigurationManager.getInstance().getValue("aimbotAVLThreshold", Integer.class).orElse(4);
    }
}

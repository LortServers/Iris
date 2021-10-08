package net.lortservers.iris.checks.aimbot;

import net.lortservers.iris.api.checks.CheckAlphabet;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.screamingsandals.lib.utils.annotations.Service;

/**
 * <p>A class representing the aimbot check type F.</p>
 */
@Service
public class AimbotCheckF extends AimbotCheck {
    /**
     * <p>Gets the check type letter.</p>
     *
     * @return the check type
     */
    @Override
    public @NonNull CheckAlphabet getType() {
        return CheckAlphabet.F;
    }
}

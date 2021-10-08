package net.lortservers.iris.checks.aimbot;

import net.lortservers.iris.api.checks.CheckAlphabet;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.screamingsandals.lib.utils.annotations.Service;

/**
 * <p>A class representing the aimbot check type E.</p>
 */
@Service
public class AimbotCheckE extends AimbotCheck {
    /**
     * <p>Gets the check type letter.</p>
     *
     * @return the check type
     */
    @Override
    public @NonNull CheckAlphabet getType() {
        return CheckAlphabet.E;
    }
}

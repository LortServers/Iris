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
}

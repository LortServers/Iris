package net.lortservers.iris.checks.aimbot;

import net.lortservers.iris.api.checks.CheckAlphabet;
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
}

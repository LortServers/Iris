package net.lortservers.iris.checks.aimbot;

import net.lortservers.iris.checks.CheckAlphabet;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.screamingsandals.lib.utils.annotations.Service;

@Service
public class AimbotCheckE extends AimbotCheck {
    @Override
    public @NonNull CheckAlphabet getType() {
        return CheckAlphabet.E;
    }

    @Override
    public int getVLThreshold() {
        return 0;
    }
}

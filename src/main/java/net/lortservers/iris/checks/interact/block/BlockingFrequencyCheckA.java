package net.lortservers.iris.checks.interact.block;

import net.lortservers.iris.checks.CheckAlphabet;
import org.checkerframework.checker.nullness.qual.NonNull;

public class BlockingFrequencyCheckA extends BlockingFrequencyCheck {
    @Override
    public @NonNull CheckAlphabet getType() {
        return CheckAlphabet.A;
    }

    @Override
    public int getVLThreshold() {
        return 0;
    }
}

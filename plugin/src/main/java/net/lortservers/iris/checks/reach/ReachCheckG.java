package net.lortservers.iris.checks.reach;

import net.lortservers.iris.api.checks.CheckAlphabet;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.screamingsandals.lib.utils.annotations.Service;

@Service
public class ReachCheckG extends ReachCheck {
    @Override
    public @NonNull CheckAlphabet getType() {
        return CheckAlphabet.G;
    }
}

package net.lortservers.iris.checks.rigidentity;

import net.lortservers.iris.api.checks.CheckAlphabet;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.screamingsandals.lib.utils.annotations.Service;

@Service
public class RigidEntityCheckB extends RigidEntityCheck {
    @Override
    public @NonNull CheckAlphabet getType() {
        return CheckAlphabet.B;
    }
}

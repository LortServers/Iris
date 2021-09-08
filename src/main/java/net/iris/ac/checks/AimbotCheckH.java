package net.iris.ac.checks;

import org.screamingsandals.lib.utils.annotations.Service;

@Service
public class AimbotCheckH extends AimbotCheck {
    @Override
    public CheckAlphabet getType() {
        return CheckAlphabet.H;
    }
}

package net.iris.ac.checks;

import net.iris.ac.utils.CheckAlphabet;
import org.screamingsandals.lib.utils.annotations.Service;

@Service
public class CriticalsCheckA extends CriticalsCheck {
    @Override
    public CheckAlphabet getType() {
        return CheckAlphabet.A;
    }
}

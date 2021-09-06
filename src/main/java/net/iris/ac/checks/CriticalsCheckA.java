package net.iris.ac.checks;

import net.iris.ac.utils.CheckAlphabet;
import org.screamingsandals.lib.tasker.TaskerTime;
import org.screamingsandals.lib.utils.annotations.Service;

@Service
public class CriticalsCheckA extends Check {
    public CriticalsCheckA() {
        super(10, 1, TaskerTime.MINUTES);
    }

    @Override
    public CheckAlphabet getType() {
        return CheckAlphabet.A;
    }

    @Override
    public String getName() {
        return "Criticals";
    }
}

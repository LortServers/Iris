package net.lortservers.iris.checks.interact;

import net.lortservers.iris.checks.CheckAlphabet;
import net.lortservers.iris.config.Configurator;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.screamingsandals.lib.plugin.ServiceManager;
import org.screamingsandals.lib.utils.annotations.Service;

@Service(dependsOn = {
        Configurator.class
})
public class InteractFrequencyCheckA extends InteractFrequencyCheck {
    @Override
    public @NonNull CheckAlphabet getType() {
        return CheckAlphabet.A;
    }

    @Override
    public int getVLThreshold() {
        return ServiceManager.get(Configurator.class).getConfig().getInteractFrequencyAVLThreshold();
    }
}

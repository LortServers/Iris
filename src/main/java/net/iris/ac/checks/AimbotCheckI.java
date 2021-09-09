package net.iris.ac.checks;

import net.iris.ac.config.Configurator;
import org.screamingsandals.lib.plugin.ServiceManager;
import org.screamingsandals.lib.utils.annotations.Service;

@Service
public class AimbotCheckI extends AimbotCheck {
    @Override
    public CheckAlphabet getType() {
        return CheckAlphabet.I;
    }

    @Override
    public int getVLThreshold() {
        return ServiceManager.get(Configurator.class).getConfig().getAimbotIVLThreshold();
    }
}

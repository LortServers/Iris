package net.lortservers.iris.checks.aimbot;

import net.lortservers.iris.checks.CheckAlphabet;
import net.lortservers.iris.config.Configurator;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.screamingsandals.lib.plugin.ServiceManager;
import org.screamingsandals.lib.utils.annotations.Service;

@Service
public class AimbotCheckG extends AimbotCheck {
    @Override
    public @NonNull CheckAlphabet getType() {
        return CheckAlphabet.G;
    }

    @Override
    public int getVLThreshold() {
        return ServiceManager.get(Configurator.class).getConfig().getAimbotGVLThreshold();
    }
}

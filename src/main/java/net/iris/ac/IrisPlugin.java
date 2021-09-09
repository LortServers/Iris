package net.iris.ac;

import net.iris.ac.checks.CheckManager;
import net.iris.ac.config.Configurator;
import net.iris.ac.listener.AimbotListener;
import net.iris.ac.utils.CooldownManager;
import net.iris.ac.utils.Punisher;
import org.screamingsandals.lib.plugin.PluginContainer;
import org.screamingsandals.lib.utils.annotations.Init;
import org.screamingsandals.lib.utils.annotations.Plugin;

@Plugin(
        id = "Iris",
        authors = {"zlataovce", "Lort533"},
        version = "1.0-SNAPSHOT"
)
@Init(services = {
        Configurator.class,
        CheckManager.class,
        CooldownManager.class,
        Punisher.class,
        // checks
        AimbotListener.class
})
public class IrisPlugin extends PluginContainer {
    private static IrisPlugin INSTANCE = null;

    @Override
    public void enable() {
        INSTANCE = this;
    }

    public static IrisPlugin getInstance() {
        if (INSTANCE == null) {
            throw new UnsupportedOperationException("Plugin is not initialized yet.");
        }
        return INSTANCE;
    }
}

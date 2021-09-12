package net.lortservers.iris;

import net.lortservers.iris.config.Configurator;
import net.lortservers.iris.utils.CooldownManager;
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
        CooldownManager.class
})
public class IrisPlugin extends PluginContainer {
    private static IrisPlugin INSTANCE = null;

    public IrisPlugin() {
        INSTANCE = this;
    }

    public static IrisPlugin getInstance() {
        if (INSTANCE == null) {
            throw new UnsupportedOperationException("Plugin is not initialized yet.");
        }
        return INSTANCE;
    }
}

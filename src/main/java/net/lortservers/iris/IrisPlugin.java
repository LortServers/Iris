package net.lortservers.iris;

import net.lortservers.iris.checks.CheckManager;
import net.lortservers.iris.config.Configurator;
import net.lortservers.iris.config.lang.IrisLangService;
import net.lortservers.iris.listener.AimbotListener;
import net.lortservers.iris.utils.CooldownManager;
import net.lortservers.iris.utils.Punisher;
import org.screamingsandals.lib.plugin.PluginContainer;
import org.screamingsandals.lib.plugin.ServiceManager;
import org.screamingsandals.lib.utils.annotations.Init;
import org.screamingsandals.lib.utils.annotations.Plugin;

@Plugin(
        id = "Iris",
        authors = {"zlataovce", "Lort533"},
        version = "1.0-SNAPSHOT"
)
@Init(services = {
        Configurator.class,
        IrisLangService.class,
        CheckManager.class,
        CooldownManager.class,
        Punisher.class
})
public class IrisPlugin extends PluginContainer {
    private static IrisPlugin INSTANCE = null;

    public IrisPlugin() {
        INSTANCE = this;
    }

    @Override
    public void enable() {
        ServiceManager.putService(new AimbotListener());
    }

    public static IrisPlugin getInstance() {
        if (INSTANCE == null) {
            throw new UnsupportedOperationException("Plugin is not initialized yet.");
        }
        return INSTANCE;
    }
}

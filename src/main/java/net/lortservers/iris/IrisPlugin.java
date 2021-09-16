package net.lortservers.iris;

import net.lortservers.iris.checks.CheckManager;
import net.lortservers.iris.config.ConfigurationManager;
import net.lortservers.iris.listener.AimbotListener;
import net.lortservers.iris.listener.InteractFrequencyListener;
import net.lortservers.iris.utils.CooldownManager;
import org.screamingsandals.lib.plugin.PluginContainer;
import org.screamingsandals.lib.tasker.Tasker;
import org.screamingsandals.lib.utils.annotations.Init;
import org.screamingsandals.lib.utils.annotations.Plugin;

/**
 * <p>The main plugin container class.</p>
 */
@Plugin(
        id = "Iris",
        authors = {"zlataovce", "Lort533"},
        version = "1.0-SNAPSHOT"
)
@Init(services = {
        Tasker.class,
        ConfigurationManager.class,
        CooldownManager.class,
        CheckManager.class,
        AimbotListener.class,
        InteractFrequencyListener.class
})
public class IrisPlugin extends PluginContainer {
    /**
     * <p>The plugin instance.</p>
     */
    private static IrisPlugin INSTANCE = null;

    /**
     * <p>Sets the plugin instance upon construction.</p>
     */
    public IrisPlugin() {
        INSTANCE = this;
    }

    /**
     * <p>Gets the plugin container instance.</p>
     *
     * @return the plugin container instance
     */
    public static IrisPlugin getInstance() {
        if (INSTANCE == null) {
            throw new UnsupportedOperationException("Plugin is not initialized yet.");
        }
        return INSTANCE;
    }
}

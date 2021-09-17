package net.lortservers.iris;

import net.lortservers.iris.checks.CheckManagerImpl;
import net.lortservers.iris.commands.AlertsCommand;
import net.lortservers.iris.commands.PlayerInfoCommand;
import net.lortservers.iris.config.ConfigurationManagerImpl;
import net.lortservers.iris.listener.AimbotListener;
import net.lortservers.iris.listener.InteractFrequencyListener;
import net.lortservers.iris.platform.BukkitEventManager;
import net.lortservers.iris.platform.EventManager;
import net.lortservers.iris.utils.CooldownManager;
import net.lortservers.iris.utils.ProtocolUtils;
import org.screamingsandals.lib.plugin.PluginContainer;
import org.screamingsandals.lib.plugin.PluginManager;
import org.screamingsandals.lib.tasker.Tasker;
import org.screamingsandals.lib.utils.PlatformType;
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
        ConfigurationManagerImpl.class,
        CooldownManager.class,
        CheckManagerImpl.class,
        AimbotListener.class,
        InteractFrequencyListener.class,
        AlertsCommand.class,
        PlayerInfoCommand.class
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

    @Override
    public void load() {
        if (PluginManager.getPlatformType() == PlatformType.BUKKIT) {
            EventManager.defaultEventManager = new BukkitEventManager();
        }
    }

    @Override
    public void enable() {
        ProtocolUtils.updateProtocols();
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
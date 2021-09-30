package net.lortservers.iris;

import net.lortservers.iris.commands.AlertsCommand;
import net.lortservers.iris.commands.PlayerInfoCommand;
import net.lortservers.iris.config.ConfigurationManagerImpl;
import net.lortservers.iris.listener.AimbotListener;
import net.lortservers.iris.listener.InteractFrequencyListener;
import net.lortservers.iris.platform.EventManager;
import net.lortservers.iris.utils.protocol.ProtocolUtils;
import net.lortservers.iris.utils.profiles.PlayerProfileManager;
import org.screamingsandals.lib.packet.PacketMapper;
import org.screamingsandals.lib.plugin.PluginContainer;
import org.screamingsandals.lib.tasker.Tasker;
import org.screamingsandals.lib.tasker.TaskerTime;
import org.screamingsandals.lib.utils.annotations.Init;
import org.screamingsandals.lib.utils.annotations.Plugin;

/**
 * <p>The main plugin container class.</p>
 */
@Plugin(
        id = "Iris",
        name = "Iris",
        authors = {"zlataovce", "Lort533"},
        version = "0.0.1-SNAPSHOT"
)
@Init(services = {
        Tasker.class,
        PacketMapper.class,
        ConfigurationManagerImpl.class,
        PlayerProfileManager.class,
        EventManager.class,
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
    public void enable() {
        Tasker.build(ProtocolUtils::updateProtocols).repeat(30, TaskerTime.MINUTES).async().start();
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

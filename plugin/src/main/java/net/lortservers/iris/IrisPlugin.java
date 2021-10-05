package net.lortservers.iris;

import net.lortservers.iris.commands.AlertsCommand;
import net.lortservers.iris.commands.JudgementDaySetCommand;
import net.lortservers.iris.commands.JudgementDayStartCommand;
import net.lortservers.iris.commands.PlayerInfoCommand;
import net.lortservers.iris.config.ConfigurationManagerImpl;
import net.lortservers.iris.listener.AimbotListener;
import net.lortservers.iris.listener.InteractFrequencyListener;
import net.lortservers.iris.platform.EventManager;
import net.lortservers.iris.utils.profiles.PlayerProfileManager;
import net.lortservers.iris.utils.protocol.ProtocolUtils;
import org.screamingsandals.lib.plugin.PluginContainer;
import org.screamingsandals.lib.tasker.Tasker;
import org.screamingsandals.lib.tasker.TaskerTime;
import org.screamingsandals.lib.utils.PlatformType;
import org.screamingsandals.lib.utils.annotations.Init;
import org.screamingsandals.lib.utils.annotations.Plugin;
import org.screamingsandals.lib.utils.annotations.PluginDependencies;

import java.util.concurrent.*;

/**
 * <p>The main plugin container class.</p>
 */
@Plugin(
        id = "Iris",
        name = "Iris",
        authors = {"zlataovce", "Lort533"},
        version = "0.0.1-SNAPSHOT"
)
@PluginDependencies(platform = PlatformType.BUKKIT, softDependencies = {
        "ViaVersion",
        "ProtocolSupport"
})
@Init(services = {
        Tasker.class,
        ConfigurationManagerImpl.class,
        PlayerProfileManager.class,
        EventManager.class,
        // listeners
        AimbotListener.class,
        InteractFrequencyListener.class,
        // commands
        AlertsCommand.class,
        PlayerInfoCommand.class,
        JudgementDaySetCommand.class,
        JudgementDayStartCommand.class
})
public class IrisPlugin extends PluginContainer {
    public static final ExecutorService THREAD_POOL = new ThreadPoolExecutor(0, 4, 60L, TimeUnit.SECONDS, new SynchronousQueue<>());
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

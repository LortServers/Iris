package net.lortservers.iris.wrap;

import net.lortservers.iris.config.ConfigurationManagerImpl;
import net.lortservers.iris.managers.ConfigurationManager;
import org.screamingsandals.lib.plugin.ServiceManager;
import org.screamingsandals.lib.utils.annotations.ServiceDependencies;

@ServiceDependencies(dependsOn = {
        ConfigurationManagerImpl.class
})
public abstract class ConfigurationDependent {
    private static final ConfigurationManager MANAGER;

    static {
        MANAGER = ServiceManager.get(ConfigurationManager.class);
    }

    protected static ConfigurationManager config() {
        return MANAGER;
    }
}

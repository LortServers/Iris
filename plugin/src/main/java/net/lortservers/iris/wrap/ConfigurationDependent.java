package net.lortservers.iris.wrap;

import net.lortservers.iris.config.ConfigurationManagerImpl;
import org.screamingsandals.lib.plugin.ServiceManager;
import org.screamingsandals.lib.utils.annotations.ServiceDependencies;

@ServiceDependencies(dependsOn = {
        ConfigurationManagerImpl.class
})
public abstract class ConfigurationDependent {
    private static final ConfigurationManagerImpl MANAGER;

    static {
        MANAGER = ServiceManager.get(ConfigurationManagerImpl.class);
    }

    protected static ConfigurationManagerImpl config() {
        return MANAGER;
    }
}

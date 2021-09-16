package net.lortservers.iris.wrap;

import net.lortservers.iris.config.ConfigurationManager;
import org.screamingsandals.lib.plugin.ServiceManager;
import org.screamingsandals.lib.utils.annotations.ServiceDependencies;

@ServiceDependencies(dependsOn = {
        ConfigurationManager.class
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

package net.lortservers.iris.managers;

import net.kyori.adventure.text.Component;
import org.screamingsandals.lib.plugin.ServiceManager;

import java.util.Map;
import java.util.Optional;

public interface ConfigurationManager {
    static ConfigurationManager getInstance() {
        return ServiceManager.get(ConfigurationManager.class);
    }

    Component getMessage(String id);

    Component getMessage(String id, Map<String, String> placeholders);

    <T> Optional<T> getValue(String key, Class<T> returnType);
}

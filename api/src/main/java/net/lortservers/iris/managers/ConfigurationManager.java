package net.lortservers.iris.managers;

import net.kyori.adventure.text.Component;
import org.screamingsandals.lib.plugin.ServiceManager;

import java.io.File;
import java.util.Map;
import java.util.Optional;

public interface ConfigurationManager {
    static ConfigurationManager getInstance() {
        return ServiceManager.get(ConfigurationManager.class);
    }

    @SuppressWarnings("rawtypes")
    Optional<FileDefinition> getTrackedFile(String relativePath);

    Component getMessage(String id);

    Component getMessage(String id, Map<String, String> placeholders);

    <T> Optional<T> getValue(String key, Class<T> returnType);

    interface FileDefinition<T> {
        Class<T> getType();
        String getRelativePath();
        T getObject();
        File toFile();
        void load();
        void save();
        boolean isLoaded();
    }
}

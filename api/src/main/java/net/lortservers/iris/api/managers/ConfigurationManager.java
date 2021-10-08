package net.lortservers.iris.api.managers;

import net.kyori.adventure.text.Component;
import net.lortservers.iris.api.checks.Check;
import net.lortservers.iris.api.utils.ThresholdType;
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

    <T> Optional<T> getTrackedFile(String file, Class<T> clazz);

    Component getMessage(String id);

    Component getMessage(String id, Map<String, String> placeholders);

    String getRawMessage(String id);

    <T> Optional<T> getValue(String key, Class<T> returnType);

    <T> Optional<T> getValue(Check check, String key, Class<T> returnType);

    boolean isCheckEnabled(Check check);

    int getVLThreshold(Check check, ThresholdType type);

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

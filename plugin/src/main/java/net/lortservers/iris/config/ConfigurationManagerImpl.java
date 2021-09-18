package net.lortservers.iris.config;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.SneakyThrows;
import net.kyori.adventure.text.Component;
import net.lortservers.iris.IrisPlugin;
import net.lortservers.iris.managers.ConfigurationManager;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.annotations.methods.OnDisable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * <p>A class responsible for managing the configuration.</p>
 */
@Service
@SuppressWarnings("rawtypes")
public class ConfigurationManagerImpl implements ConfigurationManager {
    /**
     * <p>A Jackson object mapper.</p>
     */
    public static final @NonNull ObjectMapper MAPPER = new ObjectMapper(new JsonFactory());

    private static final List<ConfigurationManager.FileDefinition> TRACKED_FILES = List.of(
            new FileDefinitionImpl<>(Configuration.class, "config.json"),
            new FileDefinitionImpl<>(Messages.class, "messages.json")
    );

    public Optional<ConfigurationManager.FileDefinition> getTrackedFile(String relativePath) {
        return TRACKED_FILES.stream().filter(e -> e.getRelativePath().equals(relativePath)).findFirst();
    }

    /**
     * <p>Gets the message component from id.</p>
     *
     * @param id the message id
     * @return the message component
     */
    public Component getMessage(String id) {
        return ((Messages) getTrackedFile("messages.json").orElseThrow().getObject()).getMessage(id);
    }

    /**
     * <p>Gets the message component from id and translates placeholders.</p>
     *
     * @param id the message id
     * @param placeholders the message placeholders
     * @return the message component
     */
    public Component getMessage(String id, Map<String, String> placeholders) {
        return ((Messages) getTrackedFile("messages.json").orElseThrow().getObject()).getMessage(id, placeholders);
    }

    public <T> Optional<T> getValue(String key, Class<T> returnType) {
        return ((Configuration) getTrackedFile("config.json").orElseThrow().getObject()).getValue(key, returnType);
    }

    /**
     * <p>Initializes the configuration.</p>
     */
    public ConfigurationManagerImpl() {
        if (!IrisPlugin.getInstance().getDataFolder().toFile().exists()) {
            //noinspection ResultOfMethodCallIgnored
            IrisPlugin.getInstance().getDataFolder().toFile().mkdirs();
        }
        for (ConfigurationManager.FileDefinition file : TRACKED_FILES) {
            file.load();
        }
    }

    /**
     * <p>Saves the configuration.</p>
     */
    @OnDisable
    public void disable() {
        for (ConfigurationManager.FileDefinition file : TRACKED_FILES) {
            file.save();
        }
    }

    @Data
    public static class FileDefinitionImpl<T> implements ConfigurationManager.FileDefinition<T> {
        private final Class<T> type;
        private final String relativePath;
        private T object;

        public File toFile() {
            return Paths.get(IrisPlugin.getInstance().getDataFolder().toAbsolutePath().toString(), relativePath.split("/")).toFile();
        }

        @SneakyThrows
        public void load() {
            if (toFile().exists() && !toFile().isDirectory()) {
                try {
                    object = MAPPER.readValue(toFile(), type);
                } catch (IOException e) {
                    object = type.cast(type.getDeclaredConstructor().newInstance());
                    IrisPlugin.getInstance().getLogger().error("Could not load " + relativePath + ".", e);
                }
            } else {
                object = type.cast(type.getDeclaredConstructor().newInstance());
            }
        }

        public void save() {
            try {
                MAPPER.writerWithDefaultPrettyPrinter().writeValue(toFile(), object);
            } catch (IOException e) {
                IrisPlugin.getInstance().getLogger().error("Could not save " + relativePath + ".", e);
            }
        }

        public boolean isLoaded() {
            return object != null;
        }
    }
}

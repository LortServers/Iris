package net.lortservers.iris.config;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.SneakyThrows;
import net.lortservers.iris.IrisPlugin;
import net.lortservers.iris.api.checks.Check;
import net.lortservers.iris.api.managers.ConfigurationManager;
import net.lortservers.iris.api.utils.ThresholdType;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.annotations.methods.OnDisable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
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
            FileDefinitionImpl.of(Configuration.class, "config.json")
    );

    @Override
    public Optional<ConfigurationManager.FileDefinition> getTrackedFile(String relativePath) {
        return TRACKED_FILES.stream().filter(e -> e.getRelativePath().equals(relativePath)).findFirst();
    }

    @Override
    public <T> Optional<T> getTrackedFile(String file, Class<T> clazz) {
        final Optional<FileDefinition> definition = getTrackedFile(file);
        if (definition.isEmpty()) {
            return Optional.empty();
        }
        return (definition.orElseThrow().isLoaded()) ? Optional.of(clazz.cast(definition.orElseThrow().getObject())) : Optional.empty();
    }

    public Optional<Configuration> getConfiguration() {
        return getTrackedFile("config.json", Configuration.class);
    }

    @Override
    public <T> Optional<T> getValue(String key, Class<T> returnType) {
        final Optional<Configuration> config = getConfiguration();
        return (config.isPresent()) ? config.orElseThrow().getValue(key, returnType) : Optional.empty();
    }

    @Override
    public <T> Optional<T> getValue(Check check, String key, Class<T> returnType) {
        final Optional<Configuration> config = getConfiguration();
        return (config.isPresent()) ? config.orElseThrow().getValue(check, key, returnType) : Optional.empty();
    }

    @Override
    public boolean isCheckEnabled(Check check) {
        final Optional<Configuration> config = getConfiguration();
        return config.isEmpty() || config.orElseThrow().isCheckEnabled(check);
    }

    @Override
    public int getVLThreshold(Check check, ThresholdType type) {
        final Optional<Configuration> config = getConfiguration();
        return (config.isPresent()) ? config.orElseThrow().getVLThreshold(check, type) : 0;
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
            IrisPlugin.THREAD_POOL.submit(file::load);
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

    @Data(staticConstructor = "of")
    public static class FileDefinitionImpl<T> implements ConfigurationManager.FileDefinition<T> {
        private final Class<T> type;
        private final String relativePath;
        private T object;

        @Override
        public File toFile() {
            return Paths.get(IrisPlugin.getInstance().getDataFolder().toAbsolutePath().toString(), relativePath.split("/")).toFile();
        }

        @Override
        @SneakyThrows
        public void load() {
            final File file = toFile();
            if (file.exists() && file.isFile()) {
                try {
                    object = MAPPER.readValue(file, type);
                } catch (IOException e) {
                    object = type.cast(type.getDeclaredConstructor().newInstance());
                    IrisPlugin.getInstance().getLogger().error("Could not load " + relativePath + ".", e);
                }
            } else {
                object = type.cast(type.getDeclaredConstructor().newInstance());
            }
        }

        @Override
        public void save() {
            final File file = toFile();
            // noinspection ResultOfMethodCallIgnored
            file.delete();
            try {
                MAPPER.writerWithDefaultPrettyPrinter().writeValue(file, object);
            } catch (IOException e) {
                IrisPlugin.getInstance().getLogger().error("Could not save " + relativePath + ".", e);
            }
        }

        @Override
        public boolean isLoaded() {
            return object != null;
        }
    }
}

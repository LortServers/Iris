package net.iris.ac.config;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.iris.ac.IrisPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.annotations.methods.OnDisable;
import org.screamingsandals.lib.utils.annotations.methods.OnEnable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

@Service
public class Configurator {
    private static final ObjectMapper MAPPER = new ObjectMapper(new JsonFactory());
    private static final File CONFIG_FILE = Paths.get(IrisPlugin.getInstance().getDataFolder().toAbsolutePath().toString(), "config.json").toFile();
    private Configuration config;

    public @NonNull Configuration getConfig() {
        return config;
    }

    @OnEnable
    public void enable() {
        if (CONFIG_FILE.exists() && !CONFIG_FILE.isDirectory()) {
            try {
                config = MAPPER.readValue(CONFIG_FILE, Configuration.class);
            } catch (IOException e) {
                IrisPlugin.getInstance().getSLF4JLogger().error("Could not load configuration.", e);
                config = new Configuration();
            }
        } else {
            config = new Configuration();
        }
    }

    @OnDisable
    public void disable() {
        if (CONFIG_FILE.exists() && !CONFIG_FILE.isDirectory()) {
            //noinspection ResultOfMethodCallIgnored
            CONFIG_FILE.delete();
        }
        try {
            MAPPER.writeValue(CONFIG_FILE, config);
        } catch (IOException e) {
            IrisPlugin.getInstance().getSLF4JLogger().error("Could not save configuration.", e);
        }
    }
}

package net.iris.ac.config;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import net.iris.ac.IrisPlugin;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.annotations.methods.OnDisable;
import org.screamingsandals.lib.utils.annotations.methods.OnEnable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

@Service
public class Configurator {
    private static final ObjectMapper MAPPER = new ObjectMapper(new JsonFactory());
    private static File CONFIG_FILE;
    @Getter
    private Configuration config;

    @OnEnable
    public void enable() {
        if (!IrisPlugin.getInstance().getDataFolder().toFile().exists()) {
            //noinspection ResultOfMethodCallIgnored
            IrisPlugin.getInstance().getDataFolder().toFile().mkdirs();
        }
        CONFIG_FILE = Paths.get(IrisPlugin.getInstance().getDataFolder().toAbsolutePath().toString(), "config.json").toFile();
        if (CONFIG_FILE.exists() && !CONFIG_FILE.isDirectory()) {
            try {
                config = MAPPER.readValue(CONFIG_FILE, Configuration.class);
            } catch (IOException e) {
                IrisPlugin.getInstance().getLogger().error("Could not load configuration.", e);
                config = new Configuration();
            }
        } else {
            config = new Configuration();
            try {
                MAPPER.writerWithDefaultPrettyPrinter().writeValue(CONFIG_FILE, config);
            } catch (IOException e) {
                IrisPlugin.getInstance().getLogger().error("Could not save configuration.", e);
            }
        }
    }

    @OnDisable
    public void disable() {
        if (CONFIG_FILE.exists() && !CONFIG_FILE.isDirectory()) {
            //noinspection ResultOfMethodCallIgnored
            CONFIG_FILE.delete();
        }
        try {
            MAPPER.writerWithDefaultPrettyPrinter().writeValue(CONFIG_FILE, config);
        } catch (IOException e) {
            IrisPlugin.getInstance().getLogger().error("Could not save configuration.", e);
        }
    }
}

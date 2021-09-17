package net.lortservers.iris.config;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.lortservers.iris.IrisPlugin;
import net.lortservers.iris.checks.CheckManagerImpl;
import net.lortservers.iris.managers.ConfigurationManager;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.annotations.methods.OnDisable;
import org.screamingsandals.lib.utils.annotations.methods.OnPostConstruct;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;

/**
 * <p>A class responsible for managing the configuration.</p>
 */
@Service(loadAfter = {
        CheckManagerImpl.class
})
public class ConfigurationManagerImpl implements ConfigurationManager {
    /**
     * <p>A Jackson object mapper.</p>
     */
    public static final @NonNull ObjectMapper MAPPER = new ObjectMapper(new JsonFactory());
    /**
     * <p>The configuration file.</p>
     */
    private static File CONFIG_FILE;
    /**
     * <p>The messages file.</p>
     */
    private static File MESSAGES_FILE;
    /**
     * <p>The configuration.</p>
     */
    @Getter
    private Configuration config;
    /**
     * <p>The messages.</p>
     */
    @Getter
    private Messages messages;

    /**
     * <p>Gets the message component from id.</p>
     *
     * @param id the message id
     * @return the message component
     */
    public Component getMessage(String id) {
        return messages.getMessage(id);
    }

    /**
     * <p>Gets the message component from id and translates placeholders.</p>
     *
     * @param id the message id
     * @param placeholders the message placeholders
     * @return the message component
     */
    public Component getMessage(String id, Map<String, String> placeholders) {
        return messages.getMessage(id, placeholders);
    }

    public <T> Optional<T> getValue(String key, Class<T> returnType) {
        return config.getValue(key, returnType);
    }

    /**
     * <p>Initializes the configuration.</p>
     */
    @OnPostConstruct
    public void construct() {
        if (!IrisPlugin.getInstance().getDataFolder().toFile().exists()) {
            //noinspection ResultOfMethodCallIgnored
            IrisPlugin.getInstance().getDataFolder().toFile().mkdirs();
        }
        CONFIG_FILE = Paths.get(IrisPlugin.getInstance().getDataFolder().toAbsolutePath().toString(), "config.json").toFile();
        MESSAGES_FILE = Paths.get(IrisPlugin.getInstance().getDataFolder().toAbsolutePath().toString(), "messages.json").toFile();
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
        if (MESSAGES_FILE.exists() && !MESSAGES_FILE.isDirectory()) {
            try {
                messages = MAPPER.readValue(MESSAGES_FILE, Messages.class);
            } catch (IOException e) {
                IrisPlugin.getInstance().getLogger().error("Could not load messages.", e);
                messages = new Messages();
            }
        } else {
            messages = new Messages();
            try {
                MAPPER.writerWithDefaultPrettyPrinter().writeValue(MESSAGES_FILE, messages);
            } catch (IOException e) {
                IrisPlugin.getInstance().getLogger().error("Could not save messages.", e);
            }
        }
    }

    /**
     * <p>Saves the configuration.</p>
     */
    @OnDisable
    public void disable() {
        if (CONFIG_FILE.exists() && !CONFIG_FILE.isDirectory()) {
            //noinspection ResultOfMethodCallIgnored
            CONFIG_FILE.delete();
        }
        if (MESSAGES_FILE.exists() && !MESSAGES_FILE.isDirectory()) {
            //noinspection ResultOfMethodCallIgnored
            MESSAGES_FILE.delete();
        }
        try {
            MAPPER.writerWithDefaultPrettyPrinter().writeValue(CONFIG_FILE, config);
        } catch (IOException e) {
            IrisPlugin.getInstance().getLogger().error("Could not save configuration.", e);
        }
        try {
            MAPPER.writerWithDefaultPrettyPrinter().writeValue(MESSAGES_FILE, messages);
        } catch (IOException e) {
            IrisPlugin.getInstance().getLogger().error("Could not save messages.", e);
        }
    }
}

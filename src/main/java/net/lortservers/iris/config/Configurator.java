package net.lortservers.iris.config;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import net.lortservers.iris.IrisPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.annotations.methods.OnDisable;
import org.screamingsandals.lib.utils.annotations.methods.OnPostConstruct;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

@Service
public class Configurator {
    public static final @NonNull ObjectMapper MAPPER = new ObjectMapper(new JsonFactory());
    private static File CONFIG_FILE;
    private static File MESSAGES_FILE;
    @Getter
    private Configuration config;
    @Getter
    private Messages messages;

    @OnPostConstruct
    public void init() {
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

package net.lortservers.iris.config;

import net.lortservers.iris.IrisPlugin;
import net.lortservers.iris.api.managers.ConfigurationManager;
import net.lortservers.iris.api.managers.TranslationManager;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.annotations.methods.OnEnable;

import java.io.File;
import java.io.IOException;

@Service
public class TranslationManagerImpl implements TranslationManager {
    private static final ConfigurationManager.FileDefinition<Messages> MESSAGES_FILE = ConfigurationManagerImpl.FileDefinitionImpl.of(Messages.class, "messages.json");
    private static final Messages DUMMY_OBJ = new Messages();
    private Messages currentTranslation = null;

    @OnEnable
    public void enable() {
        IrisPlugin.THREAD_POOL.submit(() -> {
            final File messagesFile = MESSAGES_FILE.toFile();
            if (messagesFile.exists() && messagesFile.isFile()) {
                MESSAGES_FILE.load();
                if (MESSAGES_FILE.isLoaded()) {
                    currentTranslation = MESSAGES_FILE.getObject();
                    return;
                }
            }
            try {
                final String locale = ConfigurationManager.getInstance().getValue("locale", String.class).orElse("en_US").replace("-", "_");
                currentTranslation = ConfigurationManagerImpl.MAPPER.readValue(IrisPlugin.class.getResourceAsStream("messages/" + locale + ".json"), Messages.class);
            } catch (IOException e) {
                IrisPlugin.getInstance().getLogger().error("Could not load translation, deserialization error!", e);
            } catch (IllegalArgumentException e) {
                IrisPlugin.getInstance().getLogger().error("Could not load translation, invalid locale!");
            }
        });
    }
}

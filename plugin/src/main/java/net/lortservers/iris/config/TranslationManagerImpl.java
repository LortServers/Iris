package net.lortservers.iris.config;

import net.lortservers.iris.IrisPlugin;
import net.lortservers.iris.api.managers.ConfigurationManager;
import net.lortservers.iris.api.managers.TranslationManager;
import org.apache.commons.lang3.LocaleUtils;
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
                currentTranslation = ConfigurationManagerImpl.MAPPER.readValue(IrisPlugin.class.getResourceAsStream("messages/" + LocaleUtils.toLocale(ConfigurationManager.getInstance().getValue("locale", String.class).orElse("en_US"))), Messages.class);
            } catch (IOException | IllegalArgumentException e) {
                IrisPlugin.getInstance().getLogger().info("Could not load translation (possibly invalid locale or serialization error)!");
            }
        });
    }
}

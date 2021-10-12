package net.lortservers.iris.config;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.lortservers.iris.IrisPlugin;
import net.lortservers.iris.api.managers.ConfigurationManager;
import net.lortservers.iris.api.managers.TranslationManager;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.annotations.methods.OnEnable;
import org.screamingsandals.lib.utils.reflect.Reflect;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TranslationManagerImpl implements TranslationManager {
    private static final ConfigurationManager.FileDefinition<Messages> MESSAGES_FILE = ConfigurationManagerImpl.FileDefinitionImpl.of(Messages.class, "messages.json");
    private static final Messages DUMMY = new Messages();
    private final Map<Locale, Messages> cachedTranslations = new ConcurrentHashMap<>();
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
            loadTranslation(Locale.forLanguageTag(ConfigurationManager.getInstance().getValue("locale", String.class).orElse("en")));
        });
    }

    @Override
    public void loadTranslation(Locale locale) {
        try {
            final InputStream resource = IrisPlugin.class.getResourceAsStream("lang/" + locale.toLanguageTag() + ".json");
            if (resource != null) {
                cachedTranslations.put(locale, ConfigurationManagerImpl.MAPPER.readValue(resource, Messages.class));
            } else {
                IrisPlugin.getInstance().getLogger().error("Resource 'lang/" + locale + ".json' was not found.");
            }
        } catch (IOException e) {
            IrisPlugin.getInstance().getLogger().error("Could not load translation, deserialization error!", e);
        } catch (IllegalArgumentException e) {
            IrisPlugin.getInstance().getLogger().error("Could not load translation, invalid locale!");
        }
    }

    private boolean isCurrentTranslation(Locale locale) {
        return currentTranslation != null && currentTranslation.getLocale().equals(locale);
    }

    @Override
    public @Nullable Object getTranslation(Locale locale) {
        if (!cachedTranslations.containsKey(locale) && !isCurrentTranslation(locale)) {
            loadTranslation(locale);
        }
        return (isCurrentTranslation(locale)) ? currentTranslation : cachedTranslations.get(locale);
    }

    @Override
    public @NonNull Object currentTranslation() {
        return Objects.requireNonNullElse(currentTranslation, DUMMY);
    }

    @Override
    public @NonNull Object translation(Locale locale) {
        return Objects.requireNonNullElseGet(getTranslation(locale), this::currentTranslation);
    }

    @Override
    public Component getMessage(String id) {
        return MiniMessage.miniMessage().parse(TranslationManager.componentPlaceholder(getRawMessage(id), Map.of("prefix", getRawMessage("prefix"))));
    }

    @Override
    public Component getMessage(String id, Locale locale) {
        return MiniMessage.miniMessage().parse(TranslationManager.componentPlaceholder(getRawMessage(id, locale), Map.of("prefix", getRawMessage("prefix", locale))));
    }

    @Override
    public Component getMessage(String id, Map<String, String> placeholders) {
        return MiniMessage.miniMessage().parse(TranslationManager.componentPlaceholder(getRawMessage(id), Map.of("prefix", getRawMessage("prefix"))), placeholders);
    }

    @Override
    public Component getMessage(String id, Map<String, String> placeholders, Locale locale) {
        return MiniMessage.miniMessage().parse(TranslationManager.componentPlaceholder(getRawMessage(id, locale), Map.of("prefix", getRawMessage("prefix", locale))), placeholders);
    }

    @Override
    public String getRawMessage(String id) {
        return (String) Reflect.getField(currentTranslation(), id);
    }

    @Override
    public String getRawMessage(String id, Locale locale) {
        return (String) Reflect.getField(translation(locale), id);
    }
}

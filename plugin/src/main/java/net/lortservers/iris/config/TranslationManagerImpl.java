package net.lortservers.iris.config;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.template.TemplateResolver;
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

@Service(dependsOn = {
        ConfigurationManagerImpl.class
})
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
            currentTranslation = (Messages) loadTranslation0(TranslationManager.toLocale(ConfigurationManager.getInstance().getValue("locale", String.class).orElse("en_US").replace("-", "_")));
        });
    }

    @Override
    public void loadTranslation(Locale locale) {
        final Object res = loadTranslation0(locale);
        if (res != null) {
            cachedTranslations.put(locale, (Messages) res);
        }
    }

    private @Nullable Object loadTranslation0(Locale locale) {
        try {
            final String localeStr = TranslationManager.fromLocale(locale);
            final InputStream resource = IrisPlugin.class.getResourceAsStream("/lang/" + localeStr + ".json");
            if (resource != null) {
                return ConfigurationManagerImpl.MAPPER.readValue(resource, Messages.class);
            } else {
                if (ConfigurationManager.getInstance().getValue("debug", Boolean.class).orElse(false)) {
                    IrisPlugin.getInstance().getLogger().error("Resource 'lang/" + localeStr + ".json' was not found.");
                }
            }
        } catch (IOException e) {
            IrisPlugin.getInstance().getLogger().error("Could not load translation, deserialization error!", e);
        } catch (IllegalArgumentException e) {
            IrisPlugin.getInstance().getLogger().error("Could not load translation, invalid locale!");
        }
        return null;
    }

    private boolean isCurrentTranslation(Locale locale) {
        return currentTranslation != null && currentTranslation.getLocale().equals(locale);
    }

    @Override
    public @Nullable Object getTranslation(Locale locale) {
        if (!cachedTranslations.containsKey(locale) && !isCurrentTranslation(locale)) {
            // I really don't like doing jackson on the main thread, but submitting this in the thread pool may cause multiple tasks trying to load the translation
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
        return parse(TranslationManager.componentPlaceholder(getRawMessage(id), Map.of("prefix", getRawMessage("prefix"))));
    }

    @Override
    public Component getMessage(String id, Locale locale) {
        return parse(TranslationManager.componentPlaceholder(getRawMessage(id, locale), Map.of("prefix", getRawMessage("prefix", locale))));
    }

    @Override
    public Component getMessage(String id, Map<String, String> placeholders) {
        return parse(TranslationManager.componentPlaceholder(getRawMessage(id), Map.of("prefix", getRawMessage("prefix"))), placeholders);
    }

    @Override
    public Component getMessage(String id, Map<String, String> placeholders, Locale locale) {
        return parse(TranslationManager.componentPlaceholder(getRawMessage(id, locale), Map.of("prefix", getRawMessage("prefix", locale))), placeholders);
    }

    @Override
    public String getRawMessage(String id) {
        return (String) Reflect.getField(currentTranslation(), id);
    }

    @Override
    public String getRawMessage(String id, Locale locale) {
        return (String) Reflect.getField(translation(locale), id);
    }

    private Component parse(String input) {
        return MiniMessage.miniMessage().deserialize(input);
    }

    private Component parse(String input, Map<String, String> placeholders) {
        return MiniMessage.miniMessage().deserialize(input, TemplateResolver.pairs(placeholders));
    }
}

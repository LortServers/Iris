package net.lortservers.iris.config.lang;

import lombok.SneakyThrows;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.lortservers.iris.IrisPlugin;
import net.lortservers.iris.config.Configurator;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.screamingsandals.lib.plugin.ServiceManager;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.annotations.methods.OnEnable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;

@Service(dependsOn = {
        Configurator.class
})
public class IrisLangService {
    public static final MiniMessage MINI_MESSAGE = MiniMessage.get();
    private static Path LANGUAGES_FOLDER;
    private TranslationContainer defaultLang;
    private Component defaultPrefix;

    @SneakyThrows
    @OnEnable
    public void enable() {
        LANGUAGES_FOLDER = Paths.get(IrisPlugin.getInstance().getDataFolder().toAbsolutePath().toString(), "lang");
        defaultLang = retrieveTranslationFromJar(Locale.forLanguageTag(ServiceManager.get(Configurator.class).getConfig().getLocale()));
        if (defaultLang == null) {
            defaultLang = retrieveTranslationFromJar(Locale.US);
            if (defaultLang == null) {
                throw new Exception("Could not load default translation, the jar is most likely corrupted.");
            }
        }
        defaultPrefix = MINI_MESSAGE.parse(colorCodesToMiniMessage(defaultLang.getPrefix()));
    }

    public @Nullable TranslationContainer retrieveTranslation(File file) {
        try {
            return Configurator.MAPPER.readValue(file, TranslationContainer.class);
        } catch (IOException e) {
            IrisPlugin.getInstance().getLogger().error("Could not retrieve translation from " + file.getName() + ".", e);
        }
        return null;
    }

    public @Nullable TranslationContainer retrieveTranslationFromJar(Locale loc) {
        final Path path = Paths.get(IrisPlugin.getInstance().getDataFolder().toAbsolutePath().toString(), "lang", loc.getLanguage() + ".json");
        if (!path.toFile().exists()) {
            IrisPlugin.getInstance().saveResource("lang/" + loc.getLanguage() + ".json", false);
        }
        return retrieveTranslation(path.toFile());
    }

    // TODO: finish
    public static String colorCodesToMiniMessage(String s) {
        return s.replace("&0", "<black>")
                .replace("&2", "<#006400>");
    }
}

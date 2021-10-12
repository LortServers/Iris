package net.lortservers.iris.api.managers;

import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.screamingsandals.lib.plugin.ServiceManager;

import java.util.Locale;
import java.util.Map;

public interface TranslationManager {
    static TranslationManager getInstance() {
        return ServiceManager.get(TranslationManager.class);
    }
    void loadTranslation(Locale locale);
    @Nullable
    Object getTranslation(Locale locale);
    @NonNull
    Object currentTranslation();
    @NonNull
    Object translation(Locale locale);

    Component getMessage(String id);
    Component getMessage(String id, Locale locale);
    Component getMessage(String id, Map<String, String> placeholders);
    Component getMessage(String id, Map<String, String> placeholders, Locale locale);
    String getRawMessage(String id);
    String getRawMessage(String id, Locale locale);
    static String componentPlaceholder(String s, Map<String, String> placeholders) {
        String newStr = s;
        for (Map.Entry<String, String> e : placeholders.entrySet()) {
            newStr = newStr.replaceAll("<" + e.getKey() + ">", e.getValue());
        }
        return newStr;
    }
    static String fromLocale(Locale locale) {
        return locale.getLanguage() + "_" + locale.getCountry();
    }
    static Locale toLocale(String s) {
        final String[] p = s.split("_");
        return new Locale(p[0], p[1]);
    }
}

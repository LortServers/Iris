package net.lortservers.iris.config;

import lombok.*;
import net.lortservers.iris.checks.Check;
import org.screamingsandals.lib.utils.reflect.Reflect;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;

/**
 * <p>A class holding the plugin configuration.</p>
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Configuration {
    private boolean debug = false;
    private boolean platformEvents = true;
    private int decreaseFrequency = 60;
    private int decreaseAmount = 10;
    private int cooldownPeriod = 100;
    private Map<String, Boolean> checks = Map.of(
            "Aimbot", true,
            "InteractFrequency", true,
            "BlockingFrequency", true
    );
    private Map<String, Map<String, Map<String, Integer>>> suspicionThresholds = Map.of(
            "Aimbot", Map.of(
                    "A", Map.of("message", 4),
                    "B", Map.of("message", 5),
                    "E", Map.of("message", 0),
                    "F", Map.of("message", 3),
                    "G", Map.of("message", 2),
                    "H", Map.of("message", 2),
                    "I", Map.of("message", 2)
            ),
            "InteractFrequency", Map.of(
                    "A", Map.of("message", 2)
            ),
            "BlockingFrequency", Map.of(
                    "A", Map.of("message", 5)
            )
    );
    private Map<String, Map<String, Double>> aimbot = Map.of(
            "A", Map.of(
                    "maxCountDifference", 1D,
                    "distance", 0.5
            ),
            "E", Map.of(
                    "minSimilarYaw", 11D,
                    "minSimilarPitch", 5D
            ),
            "G", Map.of(
                    "distance", 0.5
            ),
            "H", Map.of(
                    "firstDistance", 3.75,
                    "lastDistance", 3.5,
                    "count", 21D
            ),
            "I", Map.of(
                    "distance", 3.5
            )
    );
    private Map<String, Map<String, Double>> interactFrequency = Map.of(
            "A", Map.of(
                    "maxCPS", 16D
            )
    );
    private boolean discordWebhook = false;
    private String webhookUrl = "";
    private String webhookAvatar = "https://i.imgur.com/161mP3g.png";

    public <T> Optional<T> getValue(String key, Class<T> returnType) {
        final Object result = Reflect.getField(this, key);
        return (result == null) ? Optional.empty() : Optional.of(returnType.cast(result));
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<T> getValue(Check check, String key, Class<T> returnType) {
        final String fieldName = check.getName().substring(0, 1).toLowerCase(Locale.ROOT) + check.getName().substring(1);
        final Map<String, Map<String, T>> result = (Map<String, Map<String, T>>) Reflect.getField(this, fieldName);
        return (result == null) ? Optional.empty() : ((result.get(check.getType().name()) == null) ? Optional.empty() : Optional.ofNullable(result.get(check.getType().name()).get(key)));
    }

    public boolean isCheckEnabled(Check check) {
        return checks.getOrDefault(check.getName(), true);
    }

    public int getVLMessageThreshold(Check check) {
        final Map<String, Map<String, Integer>> checkBase = suspicionThresholds.get(check.getName());
        if (checkBase == null || checkBase.get(check.getType().name()) == null) {
            return 0;
        }
        return checkBase.get(check.getType().name()).getOrDefault("message", 0);
    }
}

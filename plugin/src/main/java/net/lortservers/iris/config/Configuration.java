package net.lortservers.iris.config;

import lombok.*;
import net.lortservers.iris.api.checks.Check;
import net.lortservers.iris.api.checks.CheckAlphabet;
import net.lortservers.iris.api.utils.ThresholdType;
import org.screamingsandals.lib.utils.reflect.Reflect;

import java.util.List;
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
    private String locale = "en_US";
    private boolean debug = false;
    private boolean platformEvents = true;
    private int decreaseFrequency = 60;
    private int decreaseAmount = 10;
    private int cooldownPeriod = 100;

    private List<CheckConfiguration> checks = List.of(
            CheckConfiguration.builder()
                    .name("Aimbot")
                    .type("A")
                    .options(Map.of(
                            "maxCountDifference", 1,
                            "distance", 0.5
                    ))
                    .suspicionThresholds(Map.of(
                            "message", 4
                    ))
                    .build(),
            CheckConfiguration.builder()
                    .name("Aimbot")
                    .type("B")
                    .suspicionThresholds(Map.of(
                            "message", 5
                    ))
                    .build(),
            CheckConfiguration.builder()
                    .name("Aimbot")
                    .type("E")
                    .options(Map.of(
                            "minSimilarYaw", 11,
                            "minSimilarPitch", 5
                    ))
                    .suspicionThresholds(Map.of(
                            "message", 0
                    ))
                    .build(),
            CheckConfiguration.builder()
                    .name("Aimbot")
                    .type("F")
                    .suspicionThresholds(Map.of(
                            "message", 3
                    ))
                    .build(),
            CheckConfiguration.builder()
                    .name("Aimbot")
                    .type("G")
                    .options(Map.of(
                            "distance", 0.5
                    ))
                    .suspicionThresholds(Map.of(
                            "message", 2
                    ))
                    .build(),
            CheckConfiguration.builder()
                    .name("Aimbot")
                    .type("H")
                    .options(Map.of(
                            "firstDistance", 3.75,
                            "lastDistance", 3.5,
                            "count", 21
                    ))
                    .suspicionThresholds(Map.of(
                            "message", 2
                    ))
                    .build(),
            CheckConfiguration.builder()
                    .name("Aimbot")
                    .type("I")
                    .options(Map.of(
                            "distance", 3.5
                    ))
                    .suspicionThresholds(Map.of(
                            "message", 2
                    ))
                    .build(),
            CheckConfiguration.builder()
                    .name("InteractFrequency")
                    .type("A")
                    .options(Map.of(
                            "maxCPS", 16
                    ))
                    .suspicionThresholds(Map.of(
                            "message", 2
                    ))
                    .build(),
            CheckConfiguration.builder()
                    .name("BlockingFrequency")
                    .type("A")
                    .suspicionThresholds(Map.of(
                            "message", 5
                    ))
                    .build(),
            CheckConfiguration.builder()
                    .name("RigidEntity")
                    .type("A")
                    .suspicionThresholds(Map.of(
                            "message", 4
                    ))
                    .build(),
            CheckConfiguration.builder()
                    .name("RigidEntity")
                    .type("B")
                    .suspicionThresholds(Map.of(
                            "message", 4
                    ))
                    .build(),
            CheckConfiguration.builder()
                    .name("Reach")
                    .type("F")
                    .options(Map.of(
                            "minDistance", 3.75
                    ))
                    .suspicionThresholds(Map.of(
                            "message", 4
                    ))
                    .build(),
            CheckConfiguration.builder()
                    .name("Reach")
                    .type("G")
                    .options(Map.of(
                            "minDistance", 3.75
                    ))
                    .suspicionThresholds(Map.of(
                            "message", 4
                    ))
                    .build()
    );

    private String webhookUrl = null;
    private String webhookAvatar = "https://i.imgur.com/161mP3g.png";

    public Optional<CheckConfiguration> getCheck(String name, CheckAlphabet letter) {
        return checks.stream().filter(e -> e.getName().equals(name) && e.getType().toUpperCase(Locale.ROOT).equals(letter.name())).findFirst();
    }

    public <T> Optional<T> getValue(String key, Class<T> returnType) {
        final Object result = Reflect.getField(this, key);
        return (result == null) ? Optional.empty() : Optional.of(returnType.cast(result));
    }

    public <T> Optional<T> getValue(Check check, String key, Class<T> returnType) {
        final Optional<CheckConfiguration> result = getCheck(check.getName(), check.getType());
        return (result.isEmpty()) ? Optional.empty() : Optional.of(returnType.cast(result.orElseThrow().getOptions().get(key)));
    }

    public boolean isCheckEnabled(Check check) {
        final Optional<CheckConfiguration> result = getCheck(check.getName(), check.getType());
        return result.isPresent() && result.orElseThrow().isEnabled();
    }

    public int getVLThreshold(Check check, ThresholdType type) {
        final Optional<CheckConfiguration> result = getCheck(check.getName(), check.getType());
        return (result.isEmpty()) ? 0 : result.orElseThrow().getSuspicionThresholds().getOrDefault(type.getKey(), 0);
    }
}

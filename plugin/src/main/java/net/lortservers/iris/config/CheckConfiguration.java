package net.lortservers.iris.config;

import lombok.*;
import net.lortservers.iris.api.checks.Check;
import net.lortservers.iris.api.checks.CheckAlphabet;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;

@Getter
@Builder
@EqualsAndHashCode
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CheckConfiguration {
    private String name;
    private String type;
    @Builder.Default
    private boolean enabled = true;
    @Builder.Default
    private Map<String, Object> options = Map.of();
    private Map<String, Integer> suspicionThresholds;

    public Optional<Check> toCheck() {
        return Check.get(name, CheckAlphabet.valueOf(type.toUpperCase(Locale.ROOT)));
    }
}

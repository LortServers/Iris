package net.lortservers.iris.utils;

import net.lortservers.iris.checks.Check;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.screamingsandals.lib.utils.annotations.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * <p>Class responsible for managing check cooldowns.</p>
 */
@Service
public class CooldownManager {
    /**
     * <p>Check cooldowns.</p>
     */
    private final @NonNull Map<Class<? extends Check>, Map<UUID, CooldownMapping>> cooldowns = new HashMap<>();

    /**
     * <p>Gets cooldowns for the check.</p>
     *
     * @param clazz the check class
     * @param <T> the check class type
     * @return the check cooldowns
     */
    public <T extends Check> @NonNull Map<UUID, CooldownMapping> getCooldowns(Class<T> clazz) {
        if (!cooldowns.containsKey(clazz)) {
            cooldowns.put(clazz, new HashMap<>());
        }
        return cooldowns.get(clazz);
    }
}

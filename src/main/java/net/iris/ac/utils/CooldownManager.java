package net.iris.ac.utils;

import net.iris.ac.checks.Check;
import org.screamingsandals.lib.utils.annotations.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class CooldownManager {
    private final Map<Class<? extends Check>, Map<UUID, CooldownMapping>> cooldowns = new HashMap<>();

    public <T extends Check> Map<UUID, CooldownMapping> getCooldowns(Class<T> clazz) {
        if (!cooldowns.containsKey(clazz)) {
            cooldowns.put(clazz, new HashMap<>());
        }
        return cooldowns.get(clazz);
    }
}

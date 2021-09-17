package net.lortservers.iris.checks;

import net.lortservers.iris.managers.CheckManager;
import org.screamingsandals.lib.utils.annotations.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * <p>Class responsible for managing check suspicions.</p>
 */
@Service
public class CheckManagerImpl implements CheckManager {
    /**
     * <p>VL check store.</p>
     */
    private final Map<Class<? extends Check>, Map<UUID, Integer>> vls = new HashMap<>();

    /**
     * <p>Gets VL's for the check.</p>
     *
     * @param clazz the check class
     * @param <T> the check class type
     * @return the VL's for the check
     */
    public <T extends Check> Map<UUID, Integer> getVls(Class<T> clazz) {
        if (!vls.containsKey(clazz)) {
            vls.put(clazz, new HashMap<>());
        }
        return vls.get(clazz);
    }
}

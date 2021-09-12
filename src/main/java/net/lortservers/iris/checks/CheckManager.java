package net.lortservers.iris.checks;

import net.lortservers.iris.listener.AimbotListener;
import org.screamingsandals.lib.utils.annotations.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service(loadAfter = {
        AimbotListener.class
})
public class CheckManager {
    private final Map<Class<? extends Check>, Map<UUID, Integer>> vls = new HashMap<>();

    public <T extends Check> Map<UUID, Integer> getVls(Class<T> clazz) {
        if (!vls.containsKey(clazz)) {
            vls.put(clazz, new HashMap<>());
        }
        return vls.get(clazz);
    }
}

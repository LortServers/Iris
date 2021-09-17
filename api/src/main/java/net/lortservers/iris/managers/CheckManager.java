package net.lortservers.iris.managers;

import net.lortservers.iris.checks.Check;
import org.screamingsandals.lib.plugin.ServiceManager;

import java.util.Map;
import java.util.UUID;

public interface CheckManager {
    static CheckManager getInstance() {
        return ServiceManager.get(CheckManager.class);
    }

    <T extends Check> Map<UUID, Integer> getVls(Class<T> clazz);
}

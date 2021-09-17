package net.lortservers.iris.platform;

import net.lortservers.iris.platform.events.IrisCheckTriggerEventBukkitImpl;
import net.lortservers.iris.platform.events.IrisCheckTriggerEventImpl;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.screamingsandals.lib.event.AbstractEvent;
import org.screamingsandals.lib.utils.Wrapper;
import org.screamingsandals.lib.utils.reflect.Reflect;

public class BukkitEventManager extends EventManager {
    public BukkitEventManager() {
        checkTriggerEventConverter
                .registerW2P(IrisCheckTriggerEventBukkitImpl.class, wrapper -> new IrisCheckTriggerEventBukkitImpl(wrapper.getPlayer(), wrapper.getCheck()))
                .registerP2W(IrisCheckTriggerEventImpl.class, irisCheckTriggerEvent -> new IrisCheckTriggerEventImpl(irisCheckTriggerEvent.getPlayer(), irisCheckTriggerEvent.getCheck()));
    }

    @Override
    protected <T extends AbstractEvent & Wrapper> void fireEvent0(T event) {
        final Class<Event> clazz = Reflect.getClassSafe(event.getClass().getCanonicalName().replace("Impl", "BukkitImpl"));
        if (clazz == null) {
            return;
        }
        Bukkit.getPluginManager().callEvent(event.as(clazz));
    }
}

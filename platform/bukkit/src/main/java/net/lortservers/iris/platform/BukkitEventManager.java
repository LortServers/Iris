package net.lortservers.iris.platform;

import net.lortservers.iris.platform.events.IrisCheckTriggerEventBukkitImpl;
import net.lortservers.iris.platform.events.IrisCheckTriggerEventImpl;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.screamingsandals.lib.event.AbstractEvent;
import org.screamingsandals.lib.event.Cancellable;
import org.screamingsandals.lib.utils.Wrapper;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.reflect.Reflect;

@Service
public class BukkitEventManager extends EventManager {
    public BukkitEventManager() {
        EventManager.defaultEventManager = this;

        IrisCheckTriggerEventImpl.getConverter()
                .registerW2P(IrisCheckTriggerEventBukkitImpl.class, wrapper -> new IrisCheckTriggerEventBukkitImpl(wrapper.getPlayer(), wrapper.getCheck()))
                .registerP2W(IrisCheckTriggerEventImpl.class, irisCheckTriggerEvent -> new IrisCheckTriggerEventImpl(irisCheckTriggerEvent.getPlayer(), irisCheckTriggerEvent.getCheck()));
    }

    @Override
    protected <T extends AbstractEvent & Wrapper> void fireEvent0(T event) {
        final Class<Event> clazz = Reflect.getClassSafe(event.getClass().getName().replace("Impl", "BukkitImpl"));
        if (clazz != null) {
            final Event evt = event.as(clazz);
            if (Reflect.isInstance(event, Cancellable.class)) {
                ((org.bukkit.event.Cancellable) evt).setCancelled(((Cancellable) event).isCancelled());
            }
            Bukkit.getPluginManager().callEvent(evt);
            if (Reflect.isInstance(event, Cancellable.class)) {
                if (!((Cancellable) event).isCancelled()) {
                    ((Cancellable) event).setCancelled(((org.bukkit.event.Cancellable) evt).isCancelled());
                }
            }
        }
    }
}

package net.lortservers.iris.platform;

import net.lortservers.iris.platform.events.*;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.screamingsandals.lib.event.Cancellable;
import org.screamingsandals.lib.event.SEvent;
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
        IrisCheckMessageSendEventImpl.getConverter()
                .registerW2P(IrisCheckMessageSendEventBukkitImpl.class, wrapper -> new IrisCheckMessageSendEventBukkitImpl(wrapper.getRecipients()))
                .registerP2W(IrisCheckMessageSendEventImpl.class, irisCheckMessageSendEvent -> new IrisCheckMessageSendEventImpl(irisCheckMessageSendEvent.getRecipients()));
        IrisCheckVLManipulateEventImpl.getConverter()
                .registerW2P(IrisCheckVLManipulateEventBukkitImpl.class, wrapper -> new IrisCheckVLManipulateEventBukkitImpl(wrapper.getPlayer(), wrapper.getCheck(), wrapper.getOldVL(), wrapper.getNewVL(), wrapper.isScheduled(), wrapper.getAction()))
                .registerP2W(IrisCheckVLManipulateEventImpl.class, irisCheckVLManipulateEvent -> new IrisCheckVLManipulateEventImpl(irisCheckVLManipulateEvent.getPlayer(), irisCheckVLManipulateEvent.getCheck(), irisCheckVLManipulateEvent.getOldVL(), irisCheckVLManipulateEvent.getNewVL(), irisCheckVLManipulateEvent.isScheduled(), irisCheckVLManipulateEvent.getAction()));
    }

    @Override
    protected <T extends SEvent & Wrapper> void fireEvent0(T event) {
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

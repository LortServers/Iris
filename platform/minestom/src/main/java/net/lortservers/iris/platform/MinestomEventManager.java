package net.lortservers.iris.platform;

import net.lortservers.iris.platform.events.*;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.Event;
import net.minestom.server.event.trait.CancellableEvent;
import org.screamingsandals.lib.event.Cancellable;
import org.screamingsandals.lib.event.SEvent;
import org.screamingsandals.lib.utils.Wrapper;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.reflect.Reflect;

@Service
public class MinestomEventManager extends EventManager {
    public MinestomEventManager() {
        EventManager.defaultEventManager = this;

        IrisCheckTriggerEventImpl.getConverter()
                .registerW2P(IrisCheckTriggerEventMinestomImpl.class, wrapper -> new IrisCheckTriggerEventMinestomImpl(wrapper.getPlayer(), wrapper.getCheck()))
                .registerP2W(IrisCheckTriggerEventImpl.class, irisCheckTriggerEvent -> new IrisCheckTriggerEventImpl(irisCheckTriggerEvent.getPlayer(), irisCheckTriggerEvent.getCheck()));
        IrisCheckMessageSendEventImpl.getConverter()
                .registerW2P(IrisCheckMessageSendEventMinestomImpl.class, wrapper -> new IrisCheckMessageSendEventMinestomImpl(wrapper.getRecipients()))
                .registerP2W(IrisCheckMessageSendEventImpl.class, irisCheckMessageSendEvent -> new IrisCheckMessageSendEventImpl(irisCheckMessageSendEvent.getRecipients()));
        IrisCheckVLManipulateEventImpl.getConverter()
                .registerW2P(IrisCheckVLManipulateEventMinestomImpl.class, wrapper -> new IrisCheckVLManipulateEventMinestomImpl(wrapper.getPlayer(), wrapper.getCheck(), wrapper.getOldVL(), wrapper.getNewVL(), wrapper.isScheduled(), wrapper.getAction()))
                .registerP2W(IrisCheckVLManipulateEventImpl.class, irisCheckVLManipulateEvent -> new IrisCheckVLManipulateEventImpl(irisCheckVLManipulateEvent.getPlayer(), irisCheckVLManipulateEvent.getCheck(), irisCheckVLManipulateEvent.getOldVL(), irisCheckVLManipulateEvent.getNewVL(), irisCheckVLManipulateEvent.isScheduled(), irisCheckVLManipulateEvent.getAction()));
    }

    @Override
    protected <T extends SEvent & Wrapper> void fireEvent0(T event) {
        final Class<Event> clazz = Reflect.getClassSafe(event.getClass().getName().replace("Impl", "MinestomImpl"));
        if (clazz != null) {
            final Event evt = event.as(clazz);
            if (Reflect.isInstance(event, Cancellable.class)) {
                ((CancellableEvent) evt).setCancelled(((Cancellable) event).isCancelled());
            }
            MinecraftServer.getGlobalEventHandler().call(evt);
            if (Reflect.isInstance(event, Cancellable.class)) {
                if (!((Cancellable) event).isCancelled()) {
                    ((Cancellable) event).setCancelled(((CancellableEvent) evt).isCancelled());
                }
            }
        }
    }
}

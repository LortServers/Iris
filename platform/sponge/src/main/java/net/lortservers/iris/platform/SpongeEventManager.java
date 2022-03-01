package net.lortservers.iris.platform;

import net.lortservers.iris.platform.events.*;
import org.screamingsandals.lib.event.Cancellable;
import org.screamingsandals.lib.event.SEvent;
import org.screamingsandals.lib.utils.Wrapper;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.reflect.Reflect;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Event;

@Service
public class SpongeEventManager extends EventManager {
    public SpongeEventManager() {
        EventManager.defaultEventManager = this;

        IrisCheckTriggerEventImpl.getConverter()
                .registerW2P(IrisCheckTriggerEventSpongeImpl.class, wrapper -> new IrisCheckTriggerEventSpongeImpl(wrapper.getPlayer(), wrapper.getCheck()))
                .registerP2W(IrisCheckTriggerEventImpl.class, irisCheckTriggerEvent -> new IrisCheckTriggerEventImpl(irisCheckTriggerEvent.getPlayer(), irisCheckTriggerEvent.getCheck()));
        IrisCheckMessageSendEventImpl.getConverter()
                .registerW2P(IrisCheckMessageSendEventSpongeImpl.class, wrapper -> new IrisCheckMessageSendEventSpongeImpl(wrapper.getRecipients()))
                .registerP2W(IrisCheckMessageSendEventImpl.class, irisCheckMessageSendEvent -> new IrisCheckMessageSendEventImpl(irisCheckMessageSendEvent.getRecipients()));
        IrisCheckVLManipulateEventImpl.getConverter()
                .registerW2P(IrisCheckVLManipulateEventSpongeImpl.class, wrapper -> new IrisCheckVLManipulateEventSpongeImpl(wrapper.getPlayer(), wrapper.getCheck(), wrapper.getOldVL(), wrapper.getNewVL(), wrapper.isScheduled(), wrapper.getAction()))
                .registerP2W(IrisCheckVLManipulateEventImpl.class, irisCheckVLManipulateEvent -> new IrisCheckVLManipulateEventImpl(irisCheckVLManipulateEvent.getPlayer(), irisCheckVLManipulateEvent.getCheck(), irisCheckVLManipulateEvent.getOldVL(), irisCheckVLManipulateEvent.getNewVL(), irisCheckVLManipulateEvent.isScheduled(), irisCheckVLManipulateEvent.getAction()));
    }

    @Override
    protected <T extends SEvent & Wrapper> void fireEvent0(T event) {
        final Class<Event> clazz = Reflect.getClassSafe(event.getClass().getName().replace("Impl", "SpongeImpl"));
        if (clazz != null) {
            final Event evt = event.as(clazz);
            if (Reflect.isInstance(event, Cancellable.class)) {
                ((org.spongepowered.api.event.Cancellable) evt).setCancelled(((Cancellable) event).isCancelled());
            }
            final boolean cancelled = Sponge.eventManager().post(evt);
            if (Reflect.isInstance(event, Cancellable.class)) {
                if (!((Cancellable) event).isCancelled()) {
                    ((Cancellable) event).setCancelled(cancelled);
                }
            }
        }
    }
}

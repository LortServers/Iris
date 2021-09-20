package net.lortservers.iris.platform;

import net.lortservers.iris.platform.events.IrisCheckTriggerEventImpl;
import net.lortservers.iris.platform.events.IrisCheckTriggerEventSpongeImpl;
import org.screamingsandals.lib.event.AbstractEvent;
import org.screamingsandals.lib.event.Cancellable;
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
    }

    @Override
    protected <T extends AbstractEvent & Wrapper> void fireEvent0(T event) {
        final Class<Event> clazz = Reflect.getClassSafe(event.getClass().getCanonicalName().replace("Impl", "SpongeImpl"));
        if (clazz != null) {
            final Event evt = event.as(clazz);
            if (Reflect.isInstance(event, Cancellable.class)) {
                ((org.spongepowered.api.event.Cancellable) evt).setCancelled(((Cancellable) event).isCancelled());
            }
            final boolean cancelled = Sponge.getEventManager().post(evt);
            if (Reflect.isInstance(event, Cancellable.class)) {
                if (!((Cancellable) event).isCancelled()) {
                    ((Cancellable) event).setCancelled(cancelled);
                }
            }
        }
    }
}

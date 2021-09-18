package net.lortservers.iris.platform;

import net.lortservers.iris.platform.events.IrisCheckTriggerEventImpl;
import net.lortservers.iris.platform.events.IrisCheckTriggerEventMinestomImpl;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.Event;
import org.screamingsandals.lib.event.AbstractEvent;
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
    }

    @Override
    protected <T extends AbstractEvent & Wrapper> void fireEvent0(T event) {
        final Class<Event> clazz = Reflect.getClassSafe(event.getClass().getCanonicalName().replace("Impl", "MinestomImpl"));
        if (clazz != null) {
            MinecraftServer.getGlobalEventHandler().call(event.as(clazz));
        }
    }
}
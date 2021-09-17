package net.lortservers.iris.platform;

import org.screamingsandals.lib.event.AbstractEvent;
import org.screamingsandals.lib.utils.Wrapper;

public abstract class EventManager {
    public static EventManager defaultEventManager;

    public static <T extends AbstractEvent & Wrapper> T fire(T event) {
        org.screamingsandals.lib.event.EventManager.fire(event);
        if (defaultEventManager != null) {
            defaultEventManager.fireEvent0(event);
        }
        return event;
    }

    protected abstract <T extends AbstractEvent & Wrapper> void fireEvent0(T event);
}

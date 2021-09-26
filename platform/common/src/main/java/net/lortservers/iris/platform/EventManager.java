package net.lortservers.iris.platform;

import net.lortservers.iris.managers.ConfigurationManager;
import org.screamingsandals.lib.event.AbstractEvent;
import org.screamingsandals.lib.utils.Wrapper;
import org.screamingsandals.lib.utils.annotations.AbstractService;

@AbstractService(replaceRule = "net.lortservers.iris.platform.{Platform}{className}")
public abstract class EventManager {
    protected static EventManager defaultEventManager;

    public static <T extends AbstractEvent & Wrapper> T fire(T event) {
        org.screamingsandals.lib.event.EventManager.fire(event);
        if (defaultEventManager != null && ConfigurationManager.getInstance().getValue("platformEvents", Boolean.class).orElse(true)) {
            defaultEventManager.fireEvent0(event);
        }
        return event;
    }

    protected abstract <T extends AbstractEvent & Wrapper> void fireEvent0(T event);
}

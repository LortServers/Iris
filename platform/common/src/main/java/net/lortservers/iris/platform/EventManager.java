package net.lortservers.iris.platform;

import lombok.extern.slf4j.Slf4j;
import net.lortservers.iris.managers.ConfigurationManager;
import org.screamingsandals.lib.event.AbstractEvent;
import org.screamingsandals.lib.utils.Wrapper;
import org.screamingsandals.lib.utils.annotations.AbstractService;
import org.screamingsandals.lib.utils.annotations.methods.OnEnable;

@Slf4j
@AbstractService(replaceRule = "net.lortservers.iris.platform.{Platform}{className}")
public abstract class EventManager {
    protected static EventManager defaultEventManager;
    private static boolean fireToPlatform = true;

    @OnEnable
    public void enable() {
        fireToPlatform = ConfigurationManager.getInstance().getValue("eventCompatLayer", boolean.class).orElse(true);
        if (fireToPlatform) {
            log.info("Event compatibility layer for platform " + defaultEventManager.getClass().getSimpleName().replace("EventManager", "") + " was enabled.");
        }
    }

    public static <T extends AbstractEvent & Wrapper> T fire(T event) {
        org.screamingsandals.lib.event.EventManager.fire(event);
        if (defaultEventManager != null && fireToPlatform) {
            defaultEventManager.fireEvent0(event);
        }
        return event;
    }

    protected abstract <T extends AbstractEvent & Wrapper> void fireEvent0(T event);
}

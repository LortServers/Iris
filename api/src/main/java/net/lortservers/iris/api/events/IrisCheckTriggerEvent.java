package net.lortservers.iris.api.events;

import net.lortservers.iris.api.checks.Check;

public interface IrisCheckTriggerEvent extends IrisCancellableEvent, IrisPlayerEvent {
    Check getCheck();
}

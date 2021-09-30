package net.lortservers.iris.events;

import net.lortservers.iris.checks.Check;

public interface IrisCheckTriggerEvent extends IrisCancellableEvent, IrisPlayerEvent {
    Check getCheck();
}

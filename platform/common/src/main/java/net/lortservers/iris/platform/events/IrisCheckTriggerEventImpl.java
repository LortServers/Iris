package net.lortservers.iris.platform.events;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import net.lortservers.iris.checks.Check;
import net.lortservers.iris.events.IrisCheckTriggerEvent;
import net.lortservers.iris.platform.EventManager;
import org.screamingsandals.lib.event.AbstractEvent;
import org.screamingsandals.lib.event.Cancellable;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.Wrapper;

@EqualsAndHashCode(callSuper = true)
@ToString
@RequiredArgsConstructor
public class IrisCheckTriggerEventImpl extends AbstractEvent implements IrisCheckTriggerEvent, Cancellable, Wrapper {
    private boolean cancelled;
    private final PlayerWrapper player;
    private final Check check;

    @Override
    public Check getCheck() {
        return check;
    }

    @Override
    public PlayerWrapper getPlayer() {
        return player;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public <T> T as(Class<T> type) {
        return EventManager.defaultEventManager.checkTriggerEventConverter.convert(this, type);
    }
}
package net.lortservers.iris.events;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import net.lortservers.iris.checks.Check;
import org.screamingsandals.lib.event.AbstractEvent;
import org.screamingsandals.lib.event.Cancellable;
import org.screamingsandals.lib.player.PlayerWrapper;

@EqualsAndHashCode(callSuper = true)
@ToString
@RequiredArgsConstructor
public class IrisCheckTriggerEventImpl extends AbstractEvent implements IrisCheckTriggerEvent, Cancellable {
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
}

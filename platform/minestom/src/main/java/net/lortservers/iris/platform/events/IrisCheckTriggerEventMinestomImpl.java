package net.lortservers.iris.platform.events;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import net.lortservers.iris.checks.Check;
import net.lortservers.iris.events.IrisCheckTriggerEvent;
import net.minestom.server.event.trait.CancellableEvent;
import org.screamingsandals.lib.player.PlayerWrapper;

@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
public class IrisCheckTriggerEventMinestomImpl implements IrisCheckTriggerEvent, CancellableEvent {
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

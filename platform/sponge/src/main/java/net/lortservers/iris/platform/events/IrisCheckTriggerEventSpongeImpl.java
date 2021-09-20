package net.lortservers.iris.platform.events;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import net.lortservers.iris.checks.Check;
import net.lortservers.iris.events.IrisCheckTriggerEvent;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.EventContext;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.event.impl.AbstractEvent;

@EqualsAndHashCode(callSuper = true)
@ToString
@RequiredArgsConstructor
public class IrisCheckTriggerEventSpongeImpl extends AbstractEvent implements Cancellable, IrisCheckTriggerEvent {
    private boolean cancelled = false;
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
    public @NotNull Cause getCause() {
        return Cause.of(EventContext.builder().add(EventContextKeys.PLAYER, getPlatformPlayer(Player.class)).build(), getPlatformPlayer(Player.class));
    }
}

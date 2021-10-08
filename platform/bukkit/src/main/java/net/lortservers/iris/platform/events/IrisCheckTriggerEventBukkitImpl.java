package net.lortservers.iris.platform.events;

import lombok.*;
import net.lortservers.iris.api.checks.Check;
import net.lortservers.iris.api.events.IrisCheckTriggerEvent;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.screamingsandals.lib.player.PlayerWrapper;

@Getter
@EqualsAndHashCode(callSuper = true)
@ToString
@RequiredArgsConstructor
public class IrisCheckTriggerEventBukkitImpl extends Event implements Cancellable, IrisCheckTriggerEvent {
    private static final HandlerList HANDLERS = new HandlerList();
    @Setter
    private boolean cancelled = false;
    private final PlayerWrapper player;
    private final Check check;

    @Override
    public @NonNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}

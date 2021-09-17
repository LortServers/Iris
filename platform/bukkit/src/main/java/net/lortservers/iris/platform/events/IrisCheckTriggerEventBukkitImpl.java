package net.lortservers.iris.platform.events;

import lombok.RequiredArgsConstructor;
import net.lortservers.iris.checks.Check;
import net.lortservers.iris.events.IrisCheckTriggerEvent;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.screamingsandals.lib.player.PlayerWrapper;

@RequiredArgsConstructor
public class IrisCheckTriggerEventBukkitImpl extends Event implements Cancellable, IrisCheckTriggerEvent {
    private static final HandlerList HANDLERS = new HandlerList();
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
    public @NonNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}

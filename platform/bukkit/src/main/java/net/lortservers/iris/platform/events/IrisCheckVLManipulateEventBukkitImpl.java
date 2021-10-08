package net.lortservers.iris.platform.events;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import net.lortservers.iris.api.checks.Check;
import net.lortservers.iris.api.events.IrisCheckVLManipulateEvent;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.screamingsandals.lib.player.PlayerWrapper;

@Getter
@EqualsAndHashCode(callSuper = true)
@ToString
@RequiredArgsConstructor
public class IrisCheckVLManipulateEventBukkitImpl extends Event implements IrisCheckVLManipulateEvent {
    private static final HandlerList HANDLERS = new HandlerList();
    private final PlayerWrapper player;
    private final Check check;
    private final int oldVL;
    private final int newVL;
    private final boolean scheduled;
    private final ManipulateType action;

    @Override
    public @NonNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}

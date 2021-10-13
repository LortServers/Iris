package net.lortservers.iris.platform.events;

import lombok.*;
import net.kyori.adventure.text.Component;
import net.lortservers.iris.api.events.IrisCheckMessageSendEvent;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.screamingsandals.lib.player.PlayerWrapper;

import java.util.Map;

@Getter
@EqualsAndHashCode(callSuper = true)
@ToString
@RequiredArgsConstructor
public class IrisCheckMessageSendEventBukkitImpl extends Event implements Cancellable, IrisCheckMessageSendEvent {
    private static final HandlerList HANDLERS = new HandlerList();
    @Setter
    private boolean cancelled = false;
    private final Map<PlayerWrapper, Component> recipients;

    @Override
    public @NonNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}

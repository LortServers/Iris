package net.lortservers.iris.platform.events;

import lombok.*;
import net.kyori.adventure.text.Component;
import net.lortservers.iris.api.events.IrisCheckMessageSendEvent;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.EventContext;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.event.impl.AbstractEvent;
import org.spongepowered.api.plugin.PluginContainer;

import java.util.Map;

@Getter
@EqualsAndHashCode(callSuper = true)
@ToString
@RequiredArgsConstructor
public class IrisCheckMessageSendEventSpongeImpl extends AbstractEvent implements Cancellable, IrisCheckMessageSendEvent {
    @Setter
    private boolean cancelled = false;
    private final Map<PlayerWrapper, Component> recipients;

    @Override
    public @NotNull Cause getCause() {
        final PluginContainer plugin = Sponge.getPluginManager().getPlugin("Iris").orElseThrow();
        return Cause.of(EventContext.builder().add(EventContextKeys.PLUGIN, plugin).build(), plugin);
    }
}

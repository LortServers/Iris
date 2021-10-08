package net.lortservers.iris.platform.events;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import net.lortservers.iris.api.checks.Check;
import net.lortservers.iris.api.events.IrisCheckVLManipulateEvent;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.EventContext;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.event.impl.AbstractEvent;
import org.spongepowered.api.plugin.PluginContainer;

@Getter
@EqualsAndHashCode(callSuper = true)
@ToString
@RequiredArgsConstructor
public class IrisCheckVLManipulateEventSpongeImpl extends AbstractEvent implements IrisCheckVLManipulateEvent {
    private final PlayerWrapper player;
    private final Check check;
    private final int oldVL;
    private final int newVL;
    private final boolean scheduled;
    private final ManipulateType action;

    @Override
    public @NotNull Cause getCause() {
        final PluginContainer plugin = Sponge.getPluginManager().getPlugin("Iris").orElseThrow();
        return Cause.of(EventContext.builder().add(EventContextKeys.PLUGIN, plugin).build(), plugin);
    }
}

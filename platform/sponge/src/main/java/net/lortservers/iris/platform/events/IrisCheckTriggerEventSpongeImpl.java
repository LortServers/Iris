package net.lortservers.iris.platform.events;

import lombok.*;
import net.lortservers.iris.api.checks.Check;
import net.lortservers.iris.api.events.IrisCheckTriggerEvent;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.event.Cause;
import org.spongepowered.api.event.EventContext;
import org.spongepowered.api.event.EventContextKeys;
import org.spongepowered.api.event.impl.AbstractEvent;

@Getter
@EqualsAndHashCode(callSuper = true)
@ToString
@RequiredArgsConstructor
public class IrisCheckTriggerEventSpongeImpl extends AbstractEvent implements Cancellable, IrisCheckTriggerEvent {
    @Setter
    private boolean cancelled = false;
    private final PlayerWrapper player;
    private final Check check;

    @Override
    public @NotNull Cause cause() {
        return Cause.of(EventContext.builder().add(EventContextKeys.PLAYER, getPlatformPlayer(Player.class)).build(), getPlatformPlayer(Player.class));
    }
}

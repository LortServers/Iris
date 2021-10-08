package net.lortservers.iris.platform.events;

import lombok.*;
import net.lortservers.iris.api.checks.Check;
import net.lortservers.iris.api.events.IrisCheckTriggerEvent;
import net.minestom.server.event.trait.CancellableEvent;
import org.screamingsandals.lib.player.PlayerWrapper;

@Getter
@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
public class IrisCheckTriggerEventMinestomImpl implements IrisCheckTriggerEvent, CancellableEvent {
    @Setter
    private boolean cancelled = false;
    private final PlayerWrapper player;
    private final Check check;
}

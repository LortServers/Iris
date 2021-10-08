package net.lortservers.iris.platform.events;

import lombok.*;
import net.kyori.adventure.text.Component;
import net.lortservers.iris.api.events.IrisCheckMessageSendEvent;
import net.minestom.server.event.trait.CancellableEvent;
import org.screamingsandals.lib.player.PlayerWrapper;

import java.util.List;

@Getter
@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
public class IrisCheckMessageSendEventMinestomImpl implements IrisCheckMessageSendEvent, CancellableEvent {
    @Setter
    private boolean cancelled = false;
    private final Component message;
    private final List<PlayerWrapper> recipients;
}

package net.lortservers.iris.platform.events;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import net.lortservers.iris.checks.Check;
import net.lortservers.iris.events.IrisCheckVLManipulateEvent;
import net.minestom.server.event.Event;
import org.screamingsandals.lib.player.PlayerWrapper;

@Getter
@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
public class IrisCheckVLManipulateEventMinestomImpl implements IrisCheckVLManipulateEvent, Event {
    private final PlayerWrapper player;
    private final Check check;
    private final int oldVL;
    private final int newVL;
    private final boolean scheduled;
    private final ManipulateType action;
}

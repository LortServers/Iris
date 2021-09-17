package net.lortservers.iris.events;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.lortservers.iris.checks.Check;
import org.screamingsandals.lib.event.AbstractEvent;
import org.screamingsandals.lib.player.PlayerWrapper;

@EqualsAndHashCode(callSuper = true)
@ToString
@AllArgsConstructor
public class IrisCheckTriggerEventImpl extends AbstractEvent implements IrisCheckTriggerEvent {
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
}

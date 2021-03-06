package net.lortservers.iris.platform.events;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import net.lortservers.iris.api.checks.Check;
import net.lortservers.iris.api.events.IrisCheckTriggerEvent;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.BidirectionalConverter;
import org.screamingsandals.lib.utils.Wrapper;

@Getter
@EqualsAndHashCode(callSuper = true)
@ToString
@RequiredArgsConstructor
public class IrisCheckTriggerEventImpl extends CancellableAbstractEvent implements IrisCheckTriggerEvent, Wrapper {
    @Getter
    private static final BidirectionalConverter<IrisCheckTriggerEventImpl> converter = BidirectionalConverter.build();
    private final PlayerWrapper player;
    private final Check check;

    @Override
    public <T> T as(Class<T> type) {
        return converter.convert(this, type);
    }
}

package net.lortservers.iris.platform.events;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import net.lortservers.iris.checks.Check;
import net.lortservers.iris.events.IrisCheckVLManipulateEvent;
import org.screamingsandals.lib.event.AbstractEvent;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.BidirectionalConverter;
import org.screamingsandals.lib.utils.Wrapper;

@Getter
@EqualsAndHashCode(callSuper = true)
@ToString
@RequiredArgsConstructor
public class IrisCheckVLManipulateEventImpl extends AbstractEvent implements IrisCheckVLManipulateEvent, Wrapper {
    @Getter
    private static final BidirectionalConverter<IrisCheckVLManipulateEventImpl> converter = BidirectionalConverter.build();
    private final PlayerWrapper player;
    private final Check check;
    private final int oldVL;
    private final int newVL;
    private final boolean scheduled;
    private final ManipulateType action;

    @Override
    public <T> T as(Class<T> type) {
        return converter.convert(this, type);
    }
}

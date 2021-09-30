package net.lortservers.iris.platform.events;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import net.kyori.adventure.text.Component;
import net.lortservers.iris.events.IrisCheckMessageSendEvent;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.BidirectionalConverter;
import org.screamingsandals.lib.utils.Wrapper;

import java.util.List;

@Getter
@EqualsAndHashCode(callSuper = true)
@ToString
@RequiredArgsConstructor
public class IrisCheckMessageSendEventImpl extends CancellableAbstractEvent implements IrisCheckMessageSendEvent, Wrapper {
    @Getter
    private static final BidirectionalConverter<IrisCheckMessageSendEventImpl> converter = BidirectionalConverter.build();
    private final Component message;
    private final List<PlayerWrapper> recipients;

    @Override
    public <T> T as(Class<T> type) {
        return converter.convert(this, type);
    }
}

package net.lortservers.iris.api.events;

import net.kyori.adventure.text.Component;
import org.screamingsandals.lib.player.PlayerWrapper;

import java.util.Map;

public interface IrisCheckMessageSendEvent extends IrisCancellableEvent {
    Map<PlayerWrapper, Component> getRecipients();
}

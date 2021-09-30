package net.lortservers.iris.events;

import net.kyori.adventure.text.Component;
import org.screamingsandals.lib.player.PlayerWrapper;

import java.util.List;

public interface IrisCheckMessageSendEvent extends IrisCancellableEvent {
    Component getMessage();
    List<PlayerWrapper> getRecipients();
}

package net.lortservers.iris.events;

import org.screamingsandals.lib.player.PlayerWrapper;

public interface IrisPlayerEvent {
    default <T> T getPlatformPlayer(Class<T> clazz) {
        return getPlayer().as(clazz);
    }

    PlayerWrapper getPlayer();
}

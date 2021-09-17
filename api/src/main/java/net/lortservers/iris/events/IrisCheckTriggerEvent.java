package net.lortservers.iris.events;

import net.lortservers.iris.checks.Check;
import org.screamingsandals.lib.player.PlayerWrapper;

public interface IrisCheckTriggerEvent {
    Check getCheck();

    default <T> T getPlatformPlayer(Class<T> clazz) {
        return getPlayer().as(clazz);
    }

    PlayerWrapper getPlayer();
}

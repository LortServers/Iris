package net.lortservers.iris.platform;

import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.annotations.AbstractService;

@AbstractService(replaceRule = "net.lortservers.iris.platform.{Platform}{className}")
public abstract class ProtocolManager {
    protected static ProtocolManager defaultProtocolManager;

    public static int getProtocolVersion(PlayerWrapper player) {
        if (defaultProtocolManager == null) {
            throw new UnsupportedOperationException("ProtocolManager is not initialized yet.");
        }
        return defaultProtocolManager.getProtocolVersion0(player);
    }

    protected abstract int getProtocolVersion0(PlayerWrapper player);
}

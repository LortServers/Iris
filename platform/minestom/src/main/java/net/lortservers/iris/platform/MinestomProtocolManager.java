package net.lortservers.iris.platform;

import net.minestom.server.entity.Player;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.annotations.Service;

@Service
public class MinestomProtocolManager extends ProtocolManager {
    public MinestomProtocolManager() {
        ProtocolManager.defaultProtocolManager = this;
    }

    @Override
    protected int getProtocolVersion0(PlayerWrapper player) {
        return player.as(Player.class).getPlayerConnection().getProtocolVersion();
    }
}

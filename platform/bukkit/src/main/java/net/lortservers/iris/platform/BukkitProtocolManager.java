package net.lortservers.iris.platform;

import org.screamingsandals.lib.packet.PacketMapper;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.annotations.Service;

@Service
public class BukkitProtocolManager extends ProtocolManager {
    public BukkitProtocolManager() {
        ProtocolManager.defaultProtocolManager = this;
    }

    @Override
    protected int getProtocolVersion0(PlayerWrapper player) {
        return PacketMapper.getProtocolVersion(player);
    }
}

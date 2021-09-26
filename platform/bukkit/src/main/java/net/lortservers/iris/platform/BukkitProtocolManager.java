package net.lortservers.iris.platform;

import org.screamingsandals.lib.event.OnEvent;
import org.screamingsandals.lib.event.player.SPlayerLeaveEvent;
import org.screamingsandals.lib.nms.accessors.ClientIntentionPacketAccessor;
import org.screamingsandals.lib.packet.event.SPacketEvent;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.PacketMethod;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.reflect.Reflect;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class BukkitProtocolManager extends ProtocolManager {
    private final Map<UUID, Integer> playerProtocols = new HashMap<>();

    public BukkitProtocolManager() {
        ProtocolManager.defaultProtocolManager = this;
    }

    @Override
    protected int getProtocolVersion0(PlayerWrapper player) {
        return playerProtocols.get(player.getUuid());
    }

    @OnEvent
    public void onPlayerPacket(SPacketEvent event) {
        if (event.getMethod() != PacketMethod.INBOUND) {
            return;
        }
        final Object packet = event.getPacket();
        if (ClientIntentionPacketAccessor.getType().isInstance(packet)) {
            final Object intention = Reflect.getField(packet, ClientIntentionPacketAccessor.getFieldIntention());
            if (intention.toString().equalsIgnoreCase("LOGIN")) {
                final int protocolVersion = (int) Reflect.getField(packet, ClientIntentionPacketAccessor.getFieldProtocolVersion());
                playerProtocols.put(event.getPlayer().getUuid(), protocolVersion);
            }
        }
    }

    @OnEvent
    public void onPlayerLeave(SPlayerLeaveEvent event) {
        playerProtocols.remove(event.getPlayer().getUuid());
    }
}

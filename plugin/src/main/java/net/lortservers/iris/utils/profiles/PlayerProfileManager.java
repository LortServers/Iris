package net.lortservers.iris.utils.profiles;

import net.lortservers.iris.managers.PunishmentManager;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.screamingsandals.lib.Server;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.event.OnEvent;
import org.screamingsandals.lib.event.player.SPlayerJoinEvent;
import org.screamingsandals.lib.event.player.SPlayerLeaveEvent;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.plugin.ServiceManager;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.annotations.methods.OnDisable;
import org.screamingsandals.lib.utils.annotations.methods.OnEnable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class PlayerProfileManager {
    private final Map<UUID, PlayerProfile> playerProfiles = new HashMap<>();

    public static PlayerProfileManager getInstance() {
        return ServiceManager.get(PlayerProfileManager.class);
    }

    @OnEvent(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerJoin(SPlayerJoinEvent event) {
        putPlayer(event.getPlayer());
    }

    @OnEvent(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerLeave(SPlayerLeaveEvent event) {
        removePlayer(event.getPlayer());
    }

    @OnEnable
    public void enable() {
        Server.getConnectedPlayers().forEach(this::putPlayer);
    }

    @OnDisable
    public void disable() {
        playerProfiles.clear();
    }

    private void putPlayer(PlayerWrapper player) {
        final PlayerProfile profile = PlayerProfile.of(player.getUuid());
        profile.setAlertSubscriber(PunishmentManager.canSubscribe(player));
        playerProfiles.put(player.getUuid(), profile);
    }

    private void removePlayer(PlayerWrapper player) {
        playerProfiles.remove(player.getUuid());
    }

    public static @NonNull PlayerProfile ofPlayer(PlayerWrapper player) {
        return getInstance().playerProfiles.get(player.getUuid());
    }

    public static Collection<PlayerProfile> all() {
        return getInstance().playerProfiles.values();
    }
}

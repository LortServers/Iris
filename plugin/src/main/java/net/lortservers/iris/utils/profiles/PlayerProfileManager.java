package net.lortservers.iris.utils.profiles;

import com.google.common.collect.Lists;
import net.lortservers.iris.managers.PunishmentManager;
import net.lortservers.iris.utils.profiles.persistence.FilePersistenceAdapter;
import net.lortservers.iris.utils.profiles.persistence.PersistenceAdapter;
import net.lortservers.iris.utils.profiles.persistence.PersistentPlayerProfile;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class PlayerProfileManager {
    private static final PersistenceAdapter<PersistentPlayerProfile> fallbackAdapter = new FilePersistenceAdapter();
    @Nullable
    private PersistenceAdapter<PersistentPlayerProfile> adapter = null;
    private final Map<UUID, EphemeralPlayerProfile> ephemeralPlayerProfiles = new HashMap<>();

    public static PlayerProfileManager getInstance() {
        return ServiceManager.get(PlayerProfileManager.class);
    }

    @OnEvent(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerJoin(SPlayerJoinEvent event) {
        putEphemeralPlayer(event.getPlayer());
    }

    @OnEvent(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerLeave(SPlayerLeaveEvent event) {
        ephemeralPlayerProfiles.remove(event.getPlayer().getUuid());
    }

    @OnEnable
    public void enable() {
        Server.getConnectedPlayers().forEach(this::putEphemeralPlayer);
    }

    @OnDisable
    public void disable() {
        ephemeralPlayerProfiles.clear();
    }

    private void putEphemeralPlayer(PlayerWrapper player) {
        final EphemeralPlayerProfile profile = EphemeralPlayerProfile.of(player.getUuid());
        profile.setAlertSubscriber(PunishmentManager.canSubscribe(player));
        ephemeralPlayerProfiles.put(player.getUuid(), profile);
    }

    public static @NonNull EphemeralPlayerProfile ofEphemeral(PlayerWrapper player) {
        return getInstance().ephemeralPlayerProfiles.get(player.getUuid());
    }

    public static @Nullable CompletableFuture<PersistentPlayerProfile> ofPersistent(PlayerWrapper player) {
        return (getInstance().adapter == null) ? fallbackAdapter.retrieve(player.getUuid()) : getInstance().adapter.retrieve(player.getUuid());
    }

    public static List<EphemeralPlayerProfile> allEphemeral() {
        return Lists.newArrayList(getInstance().ephemeralPlayerProfiles.values());
    }

    public static CompletableFuture<List<PersistentPlayerProfile>> allPersistent() {
        return (getInstance().adapter == null) ? fallbackAdapter.all() : getInstance().adapter.all();
    }

    public static void setAdapter(PersistenceAdapter<PersistentPlayerProfile> adapter) {
        getInstance().adapter = adapter;
    }

    public static void persist(PersistentPlayerProfile profile) {
        if (getInstance().adapter != null) {
            getInstance().adapter.persist(profile);
        }
    }
}

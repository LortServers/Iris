package net.lortservers.iris.utils.profiles;

import net.lortservers.iris.api.managers.PunishmentManager;
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

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

@Service
public class PlayerProfileManager {
    private static final PersistenceAdapter<PersistentPlayerProfile> fallbackAdapter = new FilePersistenceAdapter();
    @Nullable
    private PersistenceAdapter<PersistentPlayerProfile> adapter = null;
    private final Map<UUID, EphemeralPlayerProfile> ephemeralPlayerProfiles = new HashMap<>();

    public static PlayerProfileManager getInstance() {
        return ServiceManager.get(PlayerProfileManager.class);
    }

    @OnEvent(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(SPlayerJoinEvent event) {
        putEphemeralPlayer(event.player());
    }

    @OnEvent(priority = EventPriority.HIGHEST)
    public void onPlayerLeave(SPlayerLeaveEvent event) {
        ephemeralPlayerProfiles.remove(event.player().getUuid());
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

    public static @NonNull CompletableFuture<PersistentPlayerProfile> ofPersistent(PlayerWrapper player) {
        return Objects.requireNonNullElse(getInstance().adapter, fallbackAdapter).retrieve(player.getUuid());
    }

    public static List<EphemeralPlayerProfile> allEphemeral() {
        return new ArrayList<>(getInstance().ephemeralPlayerProfiles.values());
    }

    public static CompletableFuture<List<PersistentPlayerProfile>> allPersistent() {
        return Objects.requireNonNullElse(getInstance().adapter, fallbackAdapter).all();
    }

    public static void setAdapter(PersistenceAdapter<PersistentPlayerProfile> adapter) {
        getInstance().adapter = adapter;
    }

    public static void persist(PersistentPlayerProfile profile) {
        Objects.requireNonNullElse(getInstance().adapter, fallbackAdapter).persist(profile);
    }

    public static CompletableFuture<Void> modifyAll(Function<@NonNull PersistentPlayerProfile, @Nullable PersistentPlayerProfile> func) {
        return Objects.requireNonNullElse(getInstance().adapter, fallbackAdapter).modifyAll(func);
    }

    public static boolean hasAdapter() {
        return getInstance().adapter != null;
    }
}

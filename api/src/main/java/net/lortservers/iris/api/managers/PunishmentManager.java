package net.lortservers.iris.api.managers;

import net.lortservers.iris.api.checks.Check;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.plugin.ServiceManager;

import java.util.List;

public interface PunishmentManager {
    static PunishmentManager getInstance() {
        return ServiceManager.get(PunishmentManager.class);
    }

    default <T extends Check> void log(Object player, T check) {
        log(PlayerMapper.wrapPlayer(player), check);
    }

    default <T extends Check> void log(Object player, T check, @Nullable String info) {
        log(PlayerMapper.wrapPlayer(player), check, info);
    }

    <T extends Check> void log(PlayerWrapper player, T check);

    <T extends Check> void log(PlayerWrapper player, T check, @Nullable String info);

    void kick(PlayerWrapper player, String message);

    void ban(PlayerWrapper player, String message);

    boolean toggleAlerts(PlayerWrapper player);

    List<PlayerWrapper> getSubscribers();

    boolean isSubscribed(PlayerWrapper player);

    static boolean canSubscribe(PlayerWrapper player) {
        return player.hasPermission("iris.alerts");
    }
}

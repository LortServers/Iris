package net.lortservers.iris.managers;

import net.lortservers.iris.checks.Check;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.plugin.ServiceManager;

public interface PunishmentManager {
    static PunishmentManager getInstance() {
        return ServiceManager.get(PunishmentManager.class);
    }

    default <T extends Check> void logWarn(Object player, T check) {
        logWarn(PlayerMapper.wrapPlayer(player), check);
    }

    default <T extends Check> void logWarn(Object player, T check, @Nullable String info) {
        logWarn(PlayerMapper.wrapPlayer(player), check, info);
    }

    <T extends Check> void logWarn(PlayerWrapper player, T check);

    <T extends Check> void logWarn(PlayerWrapper player, T check, @Nullable String info);
}

package net.lortservers.iris.checks;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.player.PlayerWrapper;

public interface Check {
    CheckAlphabet getType();

    @NonNull
    String getName();

    int getVLThreshold();

    default int getVL(Object player) {
        return getVL(PlayerMapper.wrapPlayer(player));
    }

    default void increaseVL(Object player, int vl) {
        increaseVL(PlayerMapper.wrapPlayer(player), vl);
    }

    default void decreaseVL(Object player, int vl) {
        decreaseVL(PlayerMapper.wrapPlayer(player), vl);
    }

    default void resetVL(Object player) {
        resetVL(PlayerMapper.wrapPlayer(player));
    }

    default boolean isOnCooldown(Object player) {
        return isOnCooldown(PlayerMapper.wrapPlayer(player));
    }

    default void putCooldown(Object player) {
        putCooldown(PlayerMapper.wrapPlayer(player));
    }

    default boolean isEligibleForCheck(Object player) {
        return isEligibleForCheck(PlayerMapper.wrapPlayer(player));
    }

    int getVL(PlayerWrapper player);

    void increaseVL(PlayerWrapper player, int vl);

    void decreaseVL(PlayerWrapper player, int vl);

    void resetVL(PlayerWrapper player);

    boolean isOnCooldown(PlayerWrapper player);

    void putCooldown(PlayerWrapper player);

    boolean isEligibleForCheck(PlayerWrapper player);
}
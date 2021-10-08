package net.lortservers.iris.api.checks;

import net.lortservers.iris.api.utils.ThresholdType;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.plugin.ServiceManager;

public interface Check {
    CheckAlphabet getType();

    @NonNull
    String getName();

    int getVLThreshold(ThresholdType type);

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

    boolean isEnabled();

    static <T extends Check> T get(Class<T> clazz) {
        return ServiceManager.get(clazz);
    }

    @SuppressWarnings("unchecked")
    static <T extends Check> T get(@NonNull String name, CheckAlphabet letter) {
        return (T) ServiceManager.getAll(Check.class).stream().filter(e -> e.getName().equals(name) && e.getType().equals(letter)).findFirst().orElseThrow();
    }
}

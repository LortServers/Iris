package net.lortservers.iris.checks;

import lombok.RequiredArgsConstructor;
import net.lortservers.iris.config.ConfigurationManagerImpl;
import net.lortservers.iris.managers.ConfigurationManager;
import net.lortservers.iris.utils.CooldownManager;
import net.lortservers.iris.utils.CooldownMapping;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.plugin.ServiceManager;
import org.screamingsandals.lib.tasker.Tasker;
import org.screamingsandals.lib.tasker.TaskerTime;
import org.screamingsandals.lib.utils.annotations.ServiceDependencies;
import org.screamingsandals.lib.utils.annotations.methods.OnEnable;

import java.util.Locale;
import java.util.Map;
import java.util.UUID;

/**
 * <p>A check base.</p>
 */
@ServiceDependencies(dependsOn = {
        ConfigurationManagerImpl.class,
        CheckManagerImpl.class,
        CooldownManager.class
})
@RequiredArgsConstructor
public abstract class CheckImpl implements Check {
    /**
     * <p>The check's cooldown time.</p>
     */
    private final int cooldown;
    /**
     * <p>The check's periodical decreasing amount.</p>
     */
    private final int decreaseBy;
    /**
     * <p>The check's periodical decrease time repetition.</p>
     */
    private final long decreaseTime;
    /**
     * <p>The check's periodical decrease time type.</p>
     */
    private final TaskerTime decreaseTimeType;

    /**
     * <p>Constructs the check from default config values.</p>
     */
    public CheckImpl() {
        this(
                ConfigurationManager.getInstance().getValue("checkDecreaseAmount", Integer.class).orElse(10),
                ConfigurationManager.getInstance().getValue("checkDecreaseFrequency", Integer.class).orElse(60),
                ConfigurationManager.getInstance().getValue("checkCooldownPeriod", Integer.class).orElse(100),
                TaskerTime.SECONDS
        );
    }

    @OnEnable
    public void enable() {
        Tasker.build(() -> {
            final Map<UUID, Integer> vls = ServiceManager.get(CheckManagerImpl.class).getVls(CheckImpl.this.getClass());
            for (final Map.Entry<UUID, Integer> entry : vls.entrySet()) {
                int currentVl = entry.getValue();
                if (currentVl >= decreaseBy) {
                    currentVl = currentVl - decreaseBy;
                } else {
                    currentVl = 0;
                }
                vls.put(entry.getKey(), currentVl);
            }
        }).repeat(decreaseTime, decreaseTimeType).start();
    }

    /**
     * <p>Gets the check type letter.</p>
     *
     * @return the check type
     */
    public abstract @NonNull CheckAlphabet getType();

    /**
     * <p>Gets the check's name.</p>
     *
     * @return the name
     */
    public abstract @NonNull String getName();

    /**
     * <p>Gets the suspicion for this player in this check.</p>
     *
     * @param player the player
     * @return the player's VL
     */
    public int getVL(PlayerWrapper player) {
        return ServiceManager.get(CheckManagerImpl.class).getVls(getClass()).getOrDefault(player.getUuid(), 0);
    }

    /**
     * <p>Increases the suspicion for this player in this check by the supplied amount.</p>
     *
     * @param player the player
     * @param vl the VL to add
     */
    public void increaseVL(PlayerWrapper player, int vl) {
        final Map<UUID, Integer> vls = ServiceManager.get(CheckManagerImpl.class).getVls(getClass());
        vls.put(player.getUuid(), vls.getOrDefault(player.getUuid(), 0) + vl);
    }

    /**
     * <p>Decreases the suspicion for this player in this check by the supplied amount.</p>
     *
     * @param player the player
     * @param vl the VL to remove
     */
    public void decreaseVL(PlayerWrapper player, int vl) {
        final Map<UUID, Integer> vls = ServiceManager.get(CheckManagerImpl.class).getVls(getClass());
        int currentVl = vls.getOrDefault(player.getUuid(), 0);
        if (currentVl >= vl) {
            currentVl = currentVl - vl;
        } else {
            currentVl = 0;
        }
        vls.put(player.getUuid(), currentVl);
    }

    /**
     * <p>Resets the suspicion for this player in this check.</p>
     *
     * @param player the player
     */
    public void resetVL(PlayerWrapper player) {
        ServiceManager.get(CheckManagerImpl.class).getVls(this.getClass()).put(player.getUuid(), 0);
    }

    /**
     * <p>Checks if this check is on cooldown for this player.</p>
     *
     * @param player the player
     * @return is the check on cooldown for this player?
     */
    public boolean isOnCooldown(PlayerWrapper player) {
        final Map<UUID, CooldownMapping> cooldowns = ServiceManager.get(CooldownManager.class).getCooldowns(getClass());
        return cooldowns.containsKey(player.getUuid()) && cooldowns.get(player.getUuid()).isOnCooldown();
    }

    /**
     * <p>Puts this check on a cooldown for the player.</p>
     *
     * @param player the player
     */
    public void putCooldown(PlayerWrapper player) {
        final Map<UUID, CooldownMapping> cooldowns = ServiceManager.get(CooldownManager.class).getCooldowns(getClass());
        if (!cooldowns.containsKey(player.getUuid())) {
            cooldowns.put(player.getUuid(), new CooldownMapping(cooldown));
        }
        cooldowns.get(player.getUuid()).putCooldown();
    }

    /**
     * <p>Checks if the player is eligible for being checked by this check.</p>
     *
     * @param player the player to check
     * @return is eligible for this check?
     */
    public boolean isEligibleForCheck(PlayerWrapper player) {
        return player.isOnline() &&
                !player.asEntity().isDead() &&
                player.getGameMode().is("survival") &&
                !player.hasPermission("iris.bypass." + getName().toLowerCase(Locale.ROOT) + "." + getType().name().toLowerCase(Locale.ROOT));
    }

    /**
     * <p>Gets the check VL threshold.</p>
     * <p>Used for sending failed messages after the VL reaches a certain threshold.</p>
     *
     * @return the check VL threshold
     */
    public abstract int getVLThreshold();
}

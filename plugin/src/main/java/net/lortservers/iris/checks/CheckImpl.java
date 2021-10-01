package net.lortservers.iris.checks;

import lombok.RequiredArgsConstructor;
import net.lortservers.iris.config.ConfigurationManagerImpl;
import net.lortservers.iris.events.IrisCheckVLManipulateEvent;
import net.lortservers.iris.managers.ConfigurationManager;
import net.lortservers.iris.platform.EventManager;
import net.lortservers.iris.platform.events.IrisCheckVLManipulateEventImpl;
import net.lortservers.iris.utils.CooldownMapping;
import net.lortservers.iris.utils.ThresholdType;
import net.lortservers.iris.utils.profiles.PlayerProfile;
import net.lortservers.iris.utils.profiles.PlayerProfileManager;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.tasker.Tasker;
import org.screamingsandals.lib.tasker.TaskerTime;
import org.screamingsandals.lib.utils.annotations.ServiceDependencies;
import org.screamingsandals.lib.utils.annotations.methods.OnEnable;

import java.util.Locale;

/**
 * <p>A check base.</p>
 */
@ServiceDependencies(dependsOn = {
        ConfigurationManagerImpl.class,
        PlayerProfileManager.class
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
                ConfigurationManager.getInstance().getValue("decreaseAmount", Integer.class).orElse(10),
                ConfigurationManager.getInstance().getValue("decreaseFrequency", Integer.class).orElse(60),
                ConfigurationManager.getInstance().getValue("cooldownPeriod", Integer.class).orElse(100),
                TaskerTime.SECONDS
        );
    }

    @OnEnable
    public void enable() {
        Tasker.build(() -> {
            for (PlayerProfile profile : PlayerProfileManager.all()) {
                int currentVl = profile.getCheckVLs().getOrDefault(getClass(), 0);
                if (currentVl >= decreaseBy) {
                    currentVl = currentVl - decreaseBy;
                } else {
                    currentVl = 0;
                }
                EventManager.fire(new IrisCheckVLManipulateEventImpl(profile.toPlayer(), CheckImpl.this, profile.getCheckVLs().getOrDefault(getClass(), 0), currentVl, true, IrisCheckVLManipulateEvent.ManipulateType.DECREASE));
                profile.getCheckVLs().put(getClass(), currentVl);
            }
        }).repeat(decreaseTime, decreaseTimeType).start();
    }

    /**
     * <p>Gets the check type letter.</p>
     *
     * @return the check type
     */
    @Override
    public abstract @NonNull CheckAlphabet getType();

    /**
     * <p>Gets the check's name.</p>
     *
     * @return the name
     */
    @Override
    public abstract @NonNull String getName();

    /**
     * <p>Gets the suspicion for this player in this check.</p>
     *
     * @param player the player
     * @return the player's VL
     */
    @Override
    public int getVL(PlayerWrapper player) {
        return PlayerProfileManager.ofPlayer(player).getCheckVLs().getOrDefault(getClass(), 0);
    }

    /**
     * <p>Increases the suspicion for this player in this check by the supplied amount.</p>
     *
     * @param player the player
     * @param vl the VL to add
     */
    @Override
    public void increaseVL(PlayerWrapper player, int vl) {
        final PlayerProfile profile = PlayerProfileManager.ofPlayer(player);
        EventManager.fire(new IrisCheckVLManipulateEventImpl(player, this, profile.getCheckVLs().getOrDefault(getClass(), 0), profile.getCheckVLs().getOrDefault(getClass(), 0) + vl, false, IrisCheckVLManipulateEvent.ManipulateType.INCREASE));
        profile.getCheckVLs().put(getClass(), profile.getCheckVLs().getOrDefault(getClass(), 0) + vl);
    }

    /**
     * <p>Decreases the suspicion for this player in this check by the supplied amount.</p>
     *
     * @param player the player
     * @param vl the VL to remove
     */
    @Override
    public void decreaseVL(PlayerWrapper player, int vl) {
        final PlayerProfile profile = PlayerProfileManager.ofPlayer(player);
        int currentVl = profile.getCheckVLs().getOrDefault(getClass(), 0);
        if (currentVl >= vl) {
            currentVl = currentVl - vl;
        } else {
            currentVl = 0;
        }
        EventManager.fire(new IrisCheckVLManipulateEventImpl(player, this, profile.getCheckVLs().getOrDefault(getClass(), 0), currentVl, false, IrisCheckVLManipulateEvent.ManipulateType.DECREASE));
        profile.getCheckVLs().put(getClass(), currentVl);
    }

    /**
     * <p>Resets the suspicion for this player in this check.</p>
     *
     * @param player the player
     */
    @Override
    public void resetVL(PlayerWrapper player) {
        final PlayerProfile profile = PlayerProfileManager.ofPlayer(player);
        EventManager.fire(new IrisCheckVLManipulateEventImpl(player, this, profile.getCheckVLs().getOrDefault(getClass(), 0), 0, false, IrisCheckVLManipulateEvent.ManipulateType.RESET));
        profile.getCheckVLs().put(getClass(), 0);
    }

    /**
     * <p>Checks if this check is on cooldown for this player.</p>
     *
     * @param player the player
     * @return is the check on cooldown for this player?
     */
    @Override
    public boolean isOnCooldown(PlayerWrapper player) {
        final PlayerProfile profile = PlayerProfileManager.ofPlayer(player);
        return profile.getCheckCooldowns().get(getClass()) != null && profile.getCheckCooldowns().get(getClass()).isOnCooldown();
    }

    /**
     * <p>Puts this check on a cooldown for the player.</p>
     *
     * @param player the player
     */
    @Override
    public void putCooldown(PlayerWrapper player) {
        final PlayerProfile profile = PlayerProfileManager.ofPlayer(player);
        if (profile.getCheckCooldowns().get(getClass()) == null) {
            profile.getCheckCooldowns().put(getClass(), new CooldownMapping(cooldown));
        }
        profile.getCheckCooldowns().get(getClass()).putCooldown();
    }

    /**
     * <p>Checks if the player is eligible for being checked by this check.</p>
     *
     * @param player the player to check
     * @return is eligible for this check?
     */
    @Override
    public boolean isEligibleForCheck(PlayerWrapper player) {
        return player.isOnline() &&
                !player.asEntity().isDead() &&
                player.getGameMode().is("survival") &&
                !player.hasPermission("iris.bypass." + getName().toLowerCase(Locale.ROOT) + "." + getType().name().toLowerCase(Locale.ROOT)) &&
                isEnabled();
    }

    /**
     * <p>Gets the check VL threshold.</p>
     * <p>Used for sending failed messages after the VL reaches a certain threshold.</p>
     *
     * @return the check VL threshold
     */
    @Override
    public int getVLThreshold(ThresholdType type) {
        return ConfigurationManager.getInstance().getVLThreshold(this, type);
    }

    @Override
    public boolean isEnabled() {
        return ConfigurationManager.getInstance().isCheckEnabled(this);
    }
}

package net.lortservers.iris.checks;

import net.lortservers.iris.config.Configurator;
import net.lortservers.iris.utils.CooldownManager;
import net.lortservers.iris.utils.CooldownMapping;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.plugin.ServiceManager;
import org.screamingsandals.lib.tasker.Tasker;
import org.screamingsandals.lib.tasker.TaskerTime;
import org.screamingsandals.lib.utils.annotations.ServiceDependencies;

import java.util.Locale;
import java.util.Map;
import java.util.UUID;

@ServiceDependencies(dependsOn = {
        Configurator.class,
        CheckManager.class,
        CooldownManager.class
})
public abstract class Check {
    private final int cooldown;

    public Check() {
        this(
                ServiceManager.get(Configurator.class).getConfig().getCheckDecreaseAmount(),
                ServiceManager.get(Configurator.class).getConfig().getCheckDecreaseFrequency(),
                ServiceManager.get(Configurator.class).getConfig().getCheckCooldownPeriod(),
                TaskerTime.SECONDS
        );
    }

    public Check(int decreaseBy, long decreaseTime, int cooldown, TaskerTime decreaseTimeType) {
        Tasker.build(() -> {
            final Map<UUID, Integer> vls = ServiceManager.get(CheckManager.class).getVls(Check.this.getClass());
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
        this.cooldown = cooldown;
    }

    public abstract @NonNull CheckAlphabet getType();
    public abstract @NonNull String getName();

    public int getVL(PlayerWrapper player) {
        final Map<UUID, Integer> vls = ServiceManager.get(CheckManager.class).getVls(getClass());
        return vls.get(player.getUuid());
    }

    public void increaseVL(PlayerWrapper player, int vl) {
        final Map<UUID, Integer> vls = ServiceManager.get(CheckManager.class).getVls(getClass());
        vls.put(player.getUuid(), vls.getOrDefault(player.getUuid(), 0) + vl);
    }

    public void decreaseVL(PlayerWrapper player, int vl) {
        final Map<UUID, Integer> vls = ServiceManager.get(CheckManager.class).getVls(getClass());
        int currentVl = vls.getOrDefault(player.getUuid(), 0);
        if (currentVl >= vl) {
            currentVl = currentVl - vl;
        } else {
            currentVl = 0;
        }
        vls.put(player.getUuid(), currentVl);
    }

    public boolean isOnCooldown(PlayerWrapper player) {
        final Map<UUID, CooldownMapping> cooldowns = ServiceManager.get(CooldownManager.class).getCooldowns(getClass());
        return cooldowns.containsKey(player.getUuid()) && cooldowns.get(player.getUuid()).isOnCooldown();
    }

    public void putCooldown(PlayerWrapper player) {
        final Map<UUID, CooldownMapping> cooldowns = ServiceManager.get(CooldownManager.class).getCooldowns(getClass());
        if (!cooldowns.containsKey(player.getUuid())) {
            cooldowns.put(player.getUuid(), new CooldownMapping(cooldown));
        }
        cooldowns.get(player.getUuid()).putCooldown();
    }

    public boolean isEligibleForCheck(PlayerWrapper player) {
        return player.isOnline() &&
                !player.asEntity().isDead() &&
                !player.getGameMode().is("spectator", "creative") &&
                !player.hasPermission("iris.bypass." + getName().toLowerCase(Locale.ROOT) + "." + getType().name().toLowerCase(Locale.ROOT));
    }

    public abstract int getVLThreshold();
}

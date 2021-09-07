package net.iris.ac.checks;

import net.iris.ac.CheckManager;
import net.iris.ac.config.Configurator;
import net.iris.ac.utils.CheckAlphabet;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.plugin.ServiceManager;
import org.screamingsandals.lib.tasker.Tasker;
import org.screamingsandals.lib.tasker.TaskerTime;
import org.screamingsandals.lib.utils.annotations.ServiceDependencies;

import java.util.Map;
import java.util.UUID;

@ServiceDependencies(dependsOn = {
        Configurator.class,
        CheckManager.class
})
public abstract class Check {
    public Check() {
        this(
                ServiceManager.get(Configurator.class).getConfig().getCheckDecreaseAmount(),
                ServiceManager.get(Configurator.class).getConfig().getCheckDecreaseFrequency(),
                TaskerTime.SECONDS
        );
    }

    public Check(int decreaseBy, long decreaseTime) {
        this(decreaseBy, decreaseTime, TaskerTime.TICKS);
    }

    public Check(int decreaseBy, long decreaseTime, TaskerTime decreaseTimeType) {
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
    }

    public abstract CheckAlphabet getType();
    public abstract String getName();

    public void increaseVL(PlayerWrapper player, int vl) {
        final Map<UUID, Integer> vls = ServiceManager.get(CheckManager.class).getVls(this.getClass());
        vls.put(player.getUuid(), vls.getOrDefault(player.getUuid(), 0) + vl);
    }

    public void decreaseVL(PlayerWrapper player, int vl) {
        final Map<UUID, Integer> vls = ServiceManager.get(CheckManager.class).getVls(this.getClass());
        int currentVl = vls.getOrDefault(player.getUuid(), 0);
        if (currentVl >= vl) {
            currentVl = currentVl - vl;
        } else {
            currentVl = 0;
        }
        vls.put(player.getUuid(), currentVl);
    }
}

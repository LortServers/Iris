package net.iris.ac.listener;

import net.iris.ac.checks.AimbotCheckH;
import net.iris.ac.config.Configuration;
import net.iris.ac.config.Configurator;
import net.iris.ac.utils.MiscUtils;
import net.iris.ac.utils.Punisher;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityLiving;
import org.screamingsandals.lib.event.OnEvent;
import org.screamingsandals.lib.event.entity.SEntityDamageByEntityEvent;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.plugin.ServiceManager;
import org.screamingsandals.lib.tasker.Tasker;
import org.screamingsandals.lib.tasker.TaskerTime;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Service(dependsOn = {
        Configurator.class,
        AimbotCheckH.class
})
public class AimbotListener {
    @OnEvent
    public void onEntityDamageByEntity(SEntityDamageByEntityEvent event) {
        final EntityBasic attacker = event.getDamager();
        if (!attacker.getEntityType().is("minecraft:player") || !event.getEntity().getEntityType().is("minecraft:player") || !event.getDamageCause().is("attack")) {
            return;
        }
        AtomicInteger count = new AtomicInteger(0);
        AtomicReference<Float> pitch = new AtomicReference<>((float) 0);
        AtomicReference<Double> yaw = new AtomicReference<>((double) 0);
        AtomicInteger pitchcount = new AtomicInteger(0);
        AtomicInteger yawcount = new AtomicInteger(0);
        AtomicReference<Float> lastpitch = new AtomicReference<>((float) 0);
        AtomicReference<Double> lastyaw = new AtomicReference<>((double) 0);
        LocationHolder loc = attacker.getLocation();
        double r1 = Math.sqrt(attacker.getLocation().getDistanceSquared(loc));
        AtomicReference<Double> r2 = new AtomicReference<>();
        final EntityLiving target1 = MiscUtils.getTargetEntity(attacker.as(PlayerWrapper.class));
        if (target1 == null || !target1.getEntityType().is("minecraft:player") || !target1.as(PlayerWrapper.class).getUuid().equals(event.getEntity().as(PlayerWrapper.class).getUuid())) {
            return;
        }
        Tasker.build(taskBase -> () -> {
            final EntityLiving target = MiscUtils.getTargetEntity(attacker.as(PlayerWrapper.class));
            if (target == null || !target.getEntityType().is("minecraft:player") || !target.as(PlayerWrapper.class).getUuid().equals(event.getEntity().as(PlayerWrapper.class).getUuid())) {
                taskBase.cancel();
                return;
            }
            if (!attacker.as(PlayerWrapper.class).isOnline() || !event.getEntity().as(PlayerWrapper.class).isOnline()) {
                taskBase.cancel();
                return;
            }
            if (attacker.getLocation().getDistanceSquared(event.getEntity().getLocation()) >= (4.75 * 4.75)) {
                taskBase.cancel();
                return;
            }
            if (count.get() > 20) {
                count.set(21);
                taskBase.cancel();
                return;
            }
            r2.set(Math.sqrt(attacker.getLocation().getDistanceSquared(loc)));
            pitch.set((float) Math.round(attacker.getLocation().getPitch() * 10) / 10);
            if (pitch.get().equals(lastpitch.get())) {
                pitchcount.getAndIncrement();
            }
            yaw.set(Math.floor(attacker.getLocation().getYaw()));
            if (yaw.get().equals(lastyaw.get())) {
                yawcount.getAndIncrement();
            }
            lastyaw.set(yaw.get());
            lastpitch.set(pitch.get());
            count.getAndIncrement();
        }).repeat(1, TaskerTime.TICKS).stopEvent(task -> {
            final Configuration config = ServiceManager.get(Configurator.class).getConfig();
            if (r1 >= config.getAimbotHFirstDistance()) {
                if (r2.get() >= config.getAimbotHLastDistance()) {
                    if (count.get() == config.getAimbotHCount()) {
                        final AimbotCheckH h = ServiceManager.get(AimbotCheckH.class);
                        final PlayerWrapper subj = attacker.as(PlayerWrapper.class);
                        if (!h.isOnCooldown(subj) && h.isEligibleForCheck(subj)) {
                            h.increaseVL(subj, 1);
                            ServiceManager.get(Punisher.class).logWarn(subj, h);
                            h.putCooldown(subj);
                        }
                    }
                }
            }
        }).start();
    }
}

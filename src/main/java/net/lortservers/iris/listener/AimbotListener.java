package net.lortservers.iris.listener;

import net.lortservers.iris.checks.aimbot.AimbotCheckH;
import net.lortservers.iris.checks.aimbot.AimbotCheckI;
import net.lortservers.iris.config.Configuration;
import net.lortservers.iris.config.Configurator;
import net.lortservers.iris.utils.Punisher;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityLiving;
import org.screamingsandals.lib.event.OnEvent;
import org.screamingsandals.lib.event.entity.SEntityDamageByEntityEvent;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.plugin.ServiceManager;
import org.screamingsandals.lib.tasker.Tasker;
import org.screamingsandals.lib.tasker.TaskerTime;
import org.screamingsandals.lib.utils.MathUtils;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Service(dependsOn = {
        Configurator.class,
        AimbotCheckH.class,
        AimbotCheckI.class
})
public class AimbotListener {
    private final @NonNull Map<UUID, Integer> count = new HashMap<>();
    private final @NonNull Map<UUID, Integer> countAmount = new HashMap<>();

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
        double r1 = event.getEntity().getLocation().getDistanceSquared(loc);
        AtomicReference<Double> r2 = new AtomicReference<>();
        final Optional<EntityLiving> target1 = attacker.as(PlayerWrapper.class).getTarget();
        if (target1.isEmpty() || !target1.orElseThrow().getEntityType().is("minecraft:player") || !target1.orElseThrow().as(PlayerWrapper.class).getUuid().equals(event.getEntity().as(PlayerWrapper.class).getUuid())) {
            return;
        }
        Tasker.build(taskBase -> () -> {
            final Optional<EntityLiving> target = attacker.as(PlayerWrapper.class).getTarget();
            if (target.isEmpty() || !target.orElseThrow().getEntityType().is("minecraft:player") || !target.orElseThrow().as(PlayerWrapper.class).getUuid().equals(event.getEntity().as(PlayerWrapper.class).getUuid())) {
                taskBase.cancel();
                return;
            }
            if (!attacker.as(PlayerWrapper.class).isOnline() || !event.getEntity().as(PlayerWrapper.class).isOnline()) {
                taskBase.cancel();
                return;
            }
            if (attacker.getLocation().getDistanceSquared(event.getEntity().getLocation()) >= MathUtils.square(4.75)) {
                taskBase.cancel();
                return;
            }
            if (count.get() > 20) {
                count.set(21);
                taskBase.cancel();
                return;
            }
            r2.set(event.getEntity().getLocation().getDistanceSquared(loc));
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
            if (r1 >= MathUtils.square(config.getAimbotHFirstDistance())) {
                if (r2.get() >= MathUtils.square(config.getAimbotHLastDistance())) {
                    if (count.get() == config.getAimbotHCount()) {
                        final AimbotCheckH h = ServiceManager.get(AimbotCheckH.class);
                        final PlayerWrapper subj = attacker.as(PlayerWrapper.class);
                        if (!h.isOnCooldown(subj) && h.isEligibleForCheck(subj)) {
                            h.increaseVL(subj, 1);
                            if (h.getVL(subj) >= h.getVLThreshold()) {
                                ServiceManager.get(Punisher.class).logWarn(subj, h);
                            }
                            h.putCooldown(subj);
                        }
                    }
                }
            }
            if (count.get() <= 20) {
                if (r1 >= MathUtils.square(config.getAimbotIDistance())) {
                    final int attackerCount = this.count.getOrDefault(attacker.as(PlayerWrapper.class).getUuid(), 0) - count.get();
                    if ((this.count.getOrDefault(attacker.as(PlayerWrapper.class).getUuid(), 0) - count.get()) >= 1) {
                        if ((this.count.getOrDefault(attacker.as(PlayerWrapper.class).getUuid(), 0) - count.get()) <= 3) {
                            if (event.getEntity().getLocation().getY() >= attacker.getLocation().getY() && !event.getEntity().as(PlayerWrapper.class).isSprinting()) {
                                final AimbotCheckI i = ServiceManager.get(AimbotCheckI.class);
                                final PlayerWrapper subj = attacker.as(PlayerWrapper.class);
                                if (!i.isOnCooldown(subj) && i.isEligibleForCheck(subj)) {
                                    i.increaseVL(subj, 1);
                                    if (i.getVL(subj) >= i.getVLThreshold()) {
                                        ServiceManager.get(Punisher.class).logWarn(subj, i);
                                    }
                                    i.putCooldown(subj);
                                }
                            }
                        }
                    }
                    if (attackerCount > 11 && attackerCount < 15 && count.get() <= 8) {
                        final AimbotCheckI i = ServiceManager.get(AimbotCheckI.class);
                        final PlayerWrapper subj = attacker.as(PlayerWrapper.class);
                        if (!i.isOnCooldown(subj) && i.isEligibleForCheck(subj)) {
                            i.increaseVL(subj, 1);
                            if (i.getVL(subj) >= i.getVLThreshold()) {
                                if (event.getEntity().getLocation().getY() >= attacker.getLocation().getY()) {
                                    ServiceManager.get(Punisher.class).logWarn(subj, i);
                                }
                            }
                            i.putCooldown(subj);
                        }
                    }
                }
                this.count.put(attacker.as(PlayerWrapper.class).getUuid(), this.count.getOrDefault(attacker.as(PlayerWrapper.class).getUuid(), 0) + 1);
            }
        }).start();
    }
}

package net.lortservers.iris.listener;

import net.lortservers.iris.checks.aimbot.*;
import net.lortservers.iris.config.Configurator;
import net.lortservers.iris.utils.Punisher;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.screamingsandals.lib.entity.EntityLiving;
import org.screamingsandals.lib.event.OnEvent;
import org.screamingsandals.lib.event.entity.SEntityDamageByEntityEvent;
import org.screamingsandals.lib.event.player.SPlayerLeaveEvent;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.plugin.ServiceManager;
import org.screamingsandals.lib.tasker.Tasker;
import org.screamingsandals.lib.tasker.TaskerTime;
import org.screamingsandals.lib.utils.MathUtils;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.annotations.methods.OnEnable;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Service(dependsOn = {
        Configurator.class,
        AimbotCheckH.class,
        AimbotCheckI.class,
        AimbotCheckA.class,
        AimbotCheckB.class,
        AimbotCheckF.class,
        AimbotCheckG.class
})
public class AimbotListener {
    private Configurator configurator;
    private final @NonNull Map<UUID, Integer> count = new ConcurrentHashMap<>();

    @OnEnable
    public void enable() {
        configurator = ServiceManager.get(Configurator.class);
    }

    @OnEvent
    public void onPlayerLeave(SPlayerLeaveEvent event) {
        count.remove(event.getPlayer().getUuid());
    }

    @OnEvent
    public void onEntityDamageByEntity(SEntityDamageByEntityEvent event) {
        if (!event.getDamager().getEntityType().is("minecraft:player") || !event.getEntity().getEntityType().is("minecraft:player") || !event.getDamageCause().is("attack")) {
            return;
        }
        final PlayerWrapper attacker = event.getDamager().as(PlayerWrapper.class);
        final PlayerWrapper victim = event.getEntity().as(PlayerWrapper.class);
        AtomicInteger count = new AtomicInteger(0);
        AtomicReference<Float> pitch = new AtomicReference<>((float) 0);
        AtomicReference<Double> yaw = new AtomicReference<>((double) 0);
        AtomicInteger pitchcount = new AtomicInteger(0);
        AtomicInteger yawcount = new AtomicInteger(0);
        AtomicReference<Float> lastpitch = new AtomicReference<>((float) 0);
        AtomicReference<Double> lastyaw = new AtomicReference<>((double) 0);
        LocationHolder loc = attacker.getLocation();
        double r1 = victim.getLocation().getDistanceSquared(loc);
        AtomicReference<Double> r2 = new AtomicReference<>();
        final Optional<EntityLiving> target1 = attacker.getTarget();
        if (target1.isEmpty() || !target1.orElseThrow().getEntityType().is("minecraft:player") || !target1.orElseThrow().as(PlayerWrapper.class).getUuid().equals(victim.getUuid())) {
            return;
        }
        Tasker.build(taskBase -> () -> {
            final Optional<EntityLiving> target = attacker.getTarget();
            if (target.isEmpty() || !target.orElseThrow().getEntityType().is("minecraft:player") || !target.orElseThrow().as(PlayerWrapper.class).getUuid().equals(victim.getUuid())) {
                taskBase.cancel();
                return;
            }
            if (!attacker.isOnline() || !victim.isOnline()) {
                taskBase.cancel();
                return;
            }
            if (attacker.getLocation().getDistanceSquared(victim.getLocation()) >= MathUtils.square(4.75)) {
                taskBase.cancel();
                return;
            }
            if (count.get() > 20) {
                count.set(21);
                taskBase.cancel();
                return;
            }
            r2.set(victim.getLocation().getDistanceSquared(loc));
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
            if (r1 >= MathUtils.square(configurator.getConfig().getAimbotHFirstDistance())) {
                if (r2.get() >= MathUtils.square(configurator.getConfig().getAimbotHLastDistance())) {
                    if (count.get() == configurator.getConfig().getAimbotHCount()) {
                        final AimbotCheckH h = ServiceManager.get(AimbotCheckH.class);
                        if (!h.isOnCooldown(attacker) && h.isEligibleForCheck(attacker)) {
                            h.increaseVL(attacker, 1);
                            if (h.getVL(attacker) >= h.getVLThreshold()) {
                                ServiceManager.get(Punisher.class).logWarn(attacker, h);
                            }
                            h.putCooldown(attacker);
                        }
                    }
                }
            }
            if (count.get() <= 20) {
                final int attackerCount = Math.abs(this.count.getOrDefault(attacker.getUuid(), 0) - count.get());
                if (r1 >= MathUtils.square(configurator.getConfig().getAimbotIDistance())) {
                    if (attackerCount >= 1) {
                        if (attackerCount <= 3) {
                            if (victim.getLocation().getY() >= attacker.getLocation().getY() && !victim.isSprinting()) {
                                final AimbotCheckI i = ServiceManager.get(AimbotCheckI.class);
                                if (!i.isOnCooldown(attacker) && i.isEligibleForCheck(attacker)) {
                                    i.increaseVL(attacker, 1);
                                    if (i.getVL(attacker) >= i.getVLThreshold()) {
                                        ServiceManager.get(Punisher.class).logWarn(attacker, i);
                                    }
                                    i.putCooldown(attacker);
                                }
                            }
                        }
                    }
                    if (attackerCount > 11 && attackerCount < 15 && count.get() <= 8) {
                        final AimbotCheckI i = ServiceManager.get(AimbotCheckI.class);
                        if (!i.isOnCooldown(attacker) && i.isEligibleForCheck(attacker)) {
                            i.increaseVL(attacker, 1);
                            if (i.getVL(attacker) >= i.getVLThreshold()) {
                                if (victim.getLocation().getY() >= attacker.getLocation().getY()) {
                                    ServiceManager.get(Punisher.class).logWarn(attacker, i);
                                }
                            }
                            i.putCooldown(attacker);
                        }
                    }
                    if (attackerCount <= configurator.getConfig().getAimbotAMaxCountDifference()) {
                        if (attacker.getLocation().getDistanceSquared(victim.getLocation()) > MathUtils.square(configurator.getConfig().getAimbotADistance())) {
                            final AimbotCheckA a = ServiceManager.get(AimbotCheckA.class);
                            if (!a.isOnCooldown(attacker) && a.isEligibleForCheck(attacker)) {
                                a.increaseVL(attacker, 1);
                                if (a.getVL(attacker) >= a.getVLThreshold()) {
                                    ServiceManager.get(Punisher.class).logWarn(attacker, a);
                                    a.resetVL(attacker);
                                }
                                a.putCooldown(attacker);
                            }
                        }
                    } else {
                        final AimbotCheckA a = ServiceManager.get(AimbotCheckA.class);
                        a.resetVL(attacker);
                    }

                }
                this.count.put(attacker.getUuid(), count.get());
            }
            if (count.get() <= 14) {
                final AimbotCheckB b = ServiceManager.get(AimbotCheckB.class);
                if (count.get() >= 8) {
                    if (!b.isOnCooldown(attacker) && b.isEligibleForCheck(attacker)) {
                        b.increaseVL(attacker, 1);
                        if (b.getVL(attacker) >= b.getVLThreshold()) {
                            ServiceManager.get(Punisher.class).logWarn(attacker, b);
                            b.resetVL(attacker);
                        }
                        b.putCooldown(attacker);
                    }
                } else {
                    b.resetVL(attacker);
                }
                if (yawcount.get() >= configurator.getConfig().getAimbotEMinSimilarYaw() && pitchcount.get() >= configurator.getConfig().getAimbotEMinSimilarPitch()) {
                    if (r1 == r2.get()) {
                        ServiceManager.get(Punisher.class).logWarn(attacker, ServiceManager.get(AimbotCheckB.class));
                    }
                }
                final AimbotCheckF f = ServiceManager.get(AimbotCheckF.class);
                if (count.get() <= 12) {
                    if ((Math.abs(loc.getX() - attacker.getLocation().getX()) + Math.abs(loc.getZ() - attacker.getLocation().getZ())) >= 1.1) {
                        if (!f.isOnCooldown(attacker) && f.isEligibleForCheck(attacker)) {
                            f.increaseVL(attacker, 1);
                            if (f.getVL(attacker) >= f.getVLThreshold()) {
                                ServiceManager.get(Punisher.class).logWarn(attacker, f);
                                f.resetVL(attacker);
                            }
                            f.putCooldown(attacker);
                        }
                    } else {
                        f.resetVL(attacker);
                    }
                } else {
                    f.resetVL(attacker);
                }
                if (yawcount.get() < pitchcount.get()) {
                    if (attacker.getLocation().getDistanceSquared(victim.getLocation()) > MathUtils.square(configurator.getConfig().getAimbotGDistance())) {
                        final AimbotCheckG g = ServiceManager.get(AimbotCheckG.class);
                        if (!g.isOnCooldown(attacker) && g.isEligibleForCheck(attacker)) {
                            g.increaseVL(attacker, 1);
                            if (g.getVL(attacker) >= g.getVLThreshold()) {
                                ServiceManager.get(Punisher.class).logWarn(attacker, g);
                                g.resetVL(attacker);
                            }
                            g.putCooldown(attacker);
                        }
                    }
                }
            }
        }).start();
    }
}

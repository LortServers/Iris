package net.lortservers.iris.listener;

import net.lortservers.iris.checks.Check;
import net.lortservers.iris.checks.aimbot.*;
import net.lortservers.iris.config.ConfigurationManagerImpl;
import net.lortservers.iris.events.IrisCheckTriggerEvent;
import net.lortservers.iris.managers.ConfigurationManager;
import net.lortservers.iris.managers.PunishmentManager;
import net.lortservers.iris.platform.EventManager;
import net.lortservers.iris.platform.events.IrisCheckTriggerEventImpl;
import net.lortservers.iris.utils.PlayerUtils;
import net.lortservers.iris.utils.PunishmentManagerImpl;
import net.lortservers.iris.wrap.AtomicDouble;
import net.lortservers.iris.wrap.AtomicFloat;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.screamingsandals.lib.entity.EntityLiving;
import org.screamingsandals.lib.event.OnEvent;
import org.screamingsandals.lib.event.entity.SEntityDamageByEntityEvent;
import org.screamingsandals.lib.event.player.SPlayerLeaveEvent;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.tasker.Tasker;
import org.screamingsandals.lib.tasker.TaskerTime;
import org.screamingsandals.lib.utils.MathUtils;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>A class responsible for triggering aimbot checks.</p>
 */
@Service(dependsOn = {
        ConfigurationManagerImpl.class,
        PunishmentManagerImpl.class,
        AimbotCheckH.class,
        AimbotCheckI.class,
        AimbotCheckA.class,
        AimbotCheckB.class,
        AimbotCheckF.class,
        AimbotCheckG.class
})
public class AimbotListener {
    private final @NonNull Map<UUID, Integer> count = new ConcurrentHashMap<>();

    /**
     * <p>Handles player leave events for cleaning up memory.</p>
     *
     * @param event the event
     */
    @OnEvent
    public void onPlayerLeave(SPlayerLeaveEvent event) {
        count.remove(event.getPlayer().getUuid());
    }

    /**
     * <p>Handles entity damage events for processing aimbot checks.</p>
     *
     * @param event the event
     */
    @OnEvent
    public void onEntityDamageByEntity(SEntityDamageByEntityEvent event) {
        if (!event.getDamager().getEntityType().is("minecraft:player") || !event.getEntity().getEntityType().is("minecraft:player") || !event.getDamageCause().is("attack")) {
            return;
        }
        final PlayerWrapper attacker = event.getDamager().as(PlayerWrapper.class);
        final PlayerWrapper victim = event.getEntity().as(PlayerWrapper.class);
        AtomicInteger count = new AtomicInteger(0);
        AtomicFloat pitch = new AtomicFloat(0F), lastpitch = new AtomicFloat(0F);
        AtomicDouble yaw = new AtomicDouble(0D), lastyaw = new AtomicDouble(0D);
        AtomicInteger pitchcount = new AtomicInteger(0), yawcount = new AtomicInteger(0);
        LocationHolder loc = attacker.getLocation();
        double r1 = victim.getLocation().getDistanceSquared(loc);
        AtomicDouble r2 = new AtomicDouble();
        final Optional<EntityLiving> target1 = attacker.getTarget();
        if (target1.isEmpty() || !PlayerUtils.isPlayer(target1.orElseThrow()) || !target1.orElseThrow().as(PlayerWrapper.class).getUuid().equals(victim.getUuid())) {
            return;
        }
        Tasker.build(taskBase -> () -> {
            final Optional<EntityLiving> target = attacker.getTarget();
            if (target.isEmpty() || !PlayerUtils.isPlayer(target.orElseThrow()) || !target.orElseThrow().as(PlayerWrapper.class).getUuid().equals(victim.getUuid())) {
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
            if (r1 >= MathUtils.square(ConfigurationManager.getInstance().getValue("aimbotHFirstDistance", Double.class).orElse(3.75))) {
                if (r2.get() >= MathUtils.square(ConfigurationManager.getInstance().getValue("aimbotHLastDistance", Double.class).orElse(3.5))) {
                    if (count.get() == ConfigurationManager.getInstance().getValue("aimbotHCount", Integer.class).orElse(21)) {
                        final AimbotCheckH h = Check.get(AimbotCheckH.class);
                        if (!h.isOnCooldown(attacker) && h.isEligibleForCheck(attacker)) {
                            final IrisCheckTriggerEvent evt = EventManager.fire(new IrisCheckTriggerEventImpl(attacker, h));
                            if (!evt.isCancelled()) {
                                h.increaseVL(attacker, 1);
                                if (h.getVL(attacker) >= h.getVLThreshold()) {
                                    PunishmentManager.getInstance().logWarn(attacker, h);
                                }
                                h.putCooldown(attacker);
                            }
                        }
                    }
                }
            }
            if (count.get() <= 20) {
                final int attackerCount = Math.abs(this.count.getOrDefault(attacker.getUuid(), 0) - count.get());
                if (r1 >= MathUtils.square(ConfigurationManager.getInstance().getValue("aimbotIDistance", Double.class).orElse(3.5))) {
                    final AimbotCheckI i = Check.get(AimbotCheckI.class);
                    if (attackerCount >= 1 && attackerCount <= 3) {
                        if (victim.getLocation().getY() >= attacker.getLocation().getY() && !victim.isSprinting()) {
                            if (!i.isOnCooldown(attacker) && i.isEligibleForCheck(attacker)) {
                                final IrisCheckTriggerEvent evt = EventManager.fire(new IrisCheckTriggerEventImpl(attacker, i));
                                if (!evt.isCancelled()) {
                                    i.increaseVL(attacker, 1);
                                    if (i.getVL(attacker) >= i.getVLThreshold()) {
                                        PunishmentManager.getInstance().logWarn(attacker, i);
                                    }
                                    i.putCooldown(attacker);
                                }
                            }
                        }
                    }
                    if (attackerCount > 11 && attackerCount < 15 && count.get() <= 8) {
                        if (!i.isOnCooldown(attacker) && i.isEligibleForCheck(attacker)) {
                            final IrisCheckTriggerEvent evt = EventManager.fire(new IrisCheckTriggerEventImpl(attacker, i));
                            if (!evt.isCancelled()) {
                                i.increaseVL(attacker, 1);
                                if (i.getVL(attacker) >= i.getVLThreshold()) {
                                    if (victim.getLocation().getY() >= attacker.getLocation().getY()) {
                                        PunishmentManager.getInstance().logWarn(attacker, i);
                                    }
                                }
                                i.putCooldown(attacker);
                            }
                        }
                    }
                    final AimbotCheckA a = Check.get(AimbotCheckA.class);
                    if (attackerCount <= ConfigurationManager.getInstance().getValue("aimbotAMaxCountDifference", Integer.class).orElse(1)) {
                        if (attacker.getLocation().getDistanceSquared(victim.getLocation()) > MathUtils.square(ConfigurationManager.getInstance().getValue("aimbotADistance", Double.class).orElse(0.5))) {
                            if (!a.isOnCooldown(attacker) && a.isEligibleForCheck(attacker)) {
                                final IrisCheckTriggerEvent evt = EventManager.fire(new IrisCheckTriggerEventImpl(attacker, a));
                                if (!evt.isCancelled()) {
                                    a.increaseVL(attacker, 1);
                                    if (a.getVL(attacker) >= a.getVLThreshold()) {
                                        PunishmentManager.getInstance().logWarn(attacker, a);
                                        a.resetVL(attacker);
                                    }
                                    a.putCooldown(attacker);
                                }
                            }
                        }
                    } else {
                        a.resetVL(attacker);
                    }
                }
                this.count.put(attacker.getUuid(), count.get());
            }
            if (count.get() <= 14) {
                final AimbotCheckB b = Check.get(AimbotCheckB.class);
                if (count.get() >= 8) {
                    if (!b.isOnCooldown(attacker) && b.isEligibleForCheck(attacker)) {
                        final IrisCheckTriggerEvent evt = EventManager.fire(new IrisCheckTriggerEventImpl(attacker, b));
                        if (!evt.isCancelled()) {
                            b.increaseVL(attacker, 1);
                            if (b.getVL(attacker) >= b.getVLThreshold()) {
                                PunishmentManager.getInstance().logWarn(attacker, b);
                                b.resetVL(attacker);
                            }
                            b.putCooldown(attacker);
                        }
                    }
                } else {
                    b.resetVL(attacker);
                }
                if (yawcount.get() >= ConfigurationManager.getInstance().getValue("aimbotEMinSimilarYaw", Integer.class).orElse(11) && pitchcount.get() >= ConfigurationManager.getInstance().getValue("aimbotEMinSimilarPitch", Integer.class).orElse(5)) {
                    if (r1 == r2.get()) {
                        final IrisCheckTriggerEvent evt = EventManager.fire(new IrisCheckTriggerEventImpl(attacker, b));
                        if (!evt.isCancelled()) {
                            PunishmentManager.getInstance().logWarn(attacker, b);
                        }
                    }
                }
                final AimbotCheckF f = Check.get(AimbotCheckF.class);
                if (count.get() <= 12) {
                    if ((Math.abs(loc.getX() - attacker.getLocation().getX()) + Math.abs(loc.getZ() - attacker.getLocation().getZ())) >= 1.1) {
                        if (!f.isOnCooldown(attacker) && f.isEligibleForCheck(attacker)) {
                            final IrisCheckTriggerEvent evt = EventManager.fire(new IrisCheckTriggerEventImpl(attacker, f));
                            if (!evt.isCancelled()) {
                                f.increaseVL(attacker, 1);
                                if (f.getVL(attacker) >= f.getVLThreshold()) {
                                    PunishmentManager.getInstance().logWarn(attacker, f);
                                    f.resetVL(attacker);
                                }
                                f.putCooldown(attacker);
                            }
                        }
                    } else {
                        f.resetVL(attacker);
                    }
                } else {
                    f.resetVL(attacker);
                }
                if (yawcount.get() < pitchcount.get()) {
                    if (attacker.getLocation().getDistanceSquared(victim.getLocation()) > MathUtils.square(ConfigurationManager.getInstance().getValue("aimbotGDistance", Double.class).orElse(0.5))) {
                        final AimbotCheckG g = Check.get(AimbotCheckG.class);
                        if (!g.isOnCooldown(attacker) && g.isEligibleForCheck(attacker)) {
                            final IrisCheckTriggerEvent evt = EventManager.fire(new IrisCheckTriggerEventImpl(attacker, g));
                            if (!evt.isCancelled()) {
                                g.increaseVL(attacker, 1);
                                if (g.getVL(attacker) >= g.getVLThreshold()) {
                                    PunishmentManager.getInstance().logWarn(attacker, g);
                                    g.resetVL(attacker);
                                }
                                g.putCooldown(attacker);
                            }
                        }
                    }
                }
            }
        }).start();
    }
}

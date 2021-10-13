package net.lortservers.iris.listener;

import net.lortservers.iris.api.checks.Check;
import net.lortservers.iris.api.managers.ConfigurationManager;
import net.lortservers.iris.api.managers.PunishmentManager;
import net.lortservers.iris.api.utils.ThresholdType;
import net.lortservers.iris.checks.reach.ReachCheckF;
import net.lortservers.iris.checks.reach.ReachCheckG;
import net.lortservers.iris.checks.rigidentity.RigidEntityCheckA;
import net.lortservers.iris.checks.rigidentity.RigidEntityCheckB;
import net.lortservers.iris.config.ConfigurationManagerImpl;
import net.lortservers.iris.utils.PlayerUtils;
import net.lortservers.iris.utils.PunishmentManagerImpl;
import org.screamingsandals.lib.entity.EntityLiving;
import org.screamingsandals.lib.event.OnEvent;
import org.screamingsandals.lib.event.entity.SEntityDamageByEntityEvent;
import org.screamingsandals.lib.item.meta.PotionEffectHolder;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.world.LocationHolder;

@Service(dependsOn = {
        ConfigurationManagerImpl.class,
        PunishmentManagerImpl.class,
        ReachCheckG.class,
        ReachCheckF.class,
        RigidEntityCheckA.class,
        RigidEntityCheckB.class
})
public class ReachListener {
    @OnEvent
    public void onEntityDamageByEntity(SEntityDamageByEntityEvent event) {
        if (!PlayerUtils.isPlayer(event.getEntity()) || !PlayerUtils.isPlayer(event.getDamager()) || !event.getDamageCause().is("attack") || ((EntityLiving) event.getDamager()).hasPotionEffect(PotionEffectHolder.of("minecraft:speed"))) {
            return;
        }
        final PlayerWrapper attacker = event.getDamager().as(PlayerWrapper.class);
        final PlayerWrapper victim = event.getEntity().as(PlayerWrapper.class);
        if (victim.isSprinting()) {
            return;
        }
        final LocationHolder attackerLoc = attacker.getLocation();
        final LocationHolder victimLoc = victim.getLocation();
        victimLoc.setY(attackerLoc.getY());
        final double distX = Math.abs(victimLoc.getX() - attackerLoc.getX());
        final double distZ = Math.abs(victimLoc.getZ() - attackerLoc.getZ());
        final RigidEntityCheckA reA = Check.get(RigidEntityCheckA.class);
        final ReachCheckF reaF = Check.get(ReachCheckF.class);
        final RigidEntityCheckB reB = Check.get(RigidEntityCheckB.class);
        final ReachCheckG reaG = Check.get(ReachCheckG.class);
        if (distX >= ConfigurationManager.getInstance().getValue(reA, "minDistance", Double.class).orElse(3.35)) {
            if (reA.isOnCooldown(attacker) || reaF.isOnCooldown(attacker) || !reA.isEligibleForCheck(attacker) || !reaF.isEligibleForCheck(attacker)) {
                return;
            }
            reA.increaseVL(attacker, 1);
            reaF.increaseVL(attacker, 1);
            if (reaF.getVL(attacker) >= ConfigurationManager.getInstance().getVLThreshold(reaF, ThresholdType.MESSAGE) || reA.getVL(attacker) >= ConfigurationManager.getInstance().getVLThreshold(reA, ThresholdType.MESSAGE)) {
                if (distX >= ConfigurationManager.getInstance().getValue(reaF, "minDistance", Double.class).orElse(3.75)) {
                    PunishmentManager.getInstance().log(attacker, reaF, "tried to hit a player " + distX + " blocks away");
                } else {
                    PunishmentManager.getInstance().log(attacker, reA, "tried to hit a player out of it's hitbox");
                }
                event.setCancelled(true);
            }
            reA.putCooldown(attacker);
            reaF.putCooldown(attacker);
        } else if (distZ >= ConfigurationManager.getInstance().getValue(reB, "minDistance", Double.class).orElse(3.35)) {
            if (reB.isOnCooldown(attacker) || reaG.isOnCooldown(attacker) || !reB.isEligibleForCheck(attacker) || !reaG.isEligibleForCheck(attacker)) {
                return;
            }
            reB.increaseVL(attacker, 1);
            reaG.increaseVL(attacker, 1);
            if (reaG.getVL(attacker) >= ConfigurationManager.getInstance().getVLThreshold(reaG, ThresholdType.MESSAGE) || reB.getVL(attacker) >= ConfigurationManager.getInstance().getVLThreshold(reB, ThresholdType.MESSAGE)) {
                if (distZ >= ConfigurationManager.getInstance().getValue(reaG, "minDistance", Double.class).orElse(3.75)) {
                    PunishmentManager.getInstance().log(attacker, reaG, "tried to hit a player " + distZ + " blocks away");
                } else {
                    PunishmentManager.getInstance().log(attacker, reB, "tried to hit a player out of it's hitbox");
                }
                event.setCancelled(true);
            }
            reB.putCooldown(attacker);
            reaG.putCooldown(attacker);
        }
    }
}

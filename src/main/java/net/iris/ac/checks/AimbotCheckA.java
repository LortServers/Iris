package net.iris.ac.checks;

import net.iris.ac.utils.CheckAlphabet;
import net.iris.ac.utils.MiscUtils;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityLiving;
import org.screamingsandals.lib.event.OnEvent;
import org.screamingsandals.lib.event.entity.SEntityDamageByEntityEvent;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.world.LocationHolder;

@Service
public class AimbotCheckA extends AimbotCheck {
    @Override
    public CheckAlphabet getType() {
        return CheckAlphabet.A;
    }

    @OnEvent
    public void onEntityDamageByEntity(SEntityDamageByEntityEvent event) {
        final EntityBasic attacker = event.getDamager();
        if (!attacker.getEntityType().is("minecraft:player") || !event.getEntity().getEntityType().is("minecraft:player") || !event.getDamageCause().is("attack")) {
            return;
        }
        if (this.isOnCooldown(attacker.as(PlayerWrapper.class))) {
            return;
        }
        int count, yaw, pitch = 0;
        LocationHolder loc = attacker.getLocation();
        double r1 = Math.sqrt(attacker.getLocation().getDistanceSquared(loc));
        final EntityLiving target = MiscUtils.getTargetEntity(attacker.as(PlayerWrapper.class));
        if (target == null || !target.getEntityType().is("minecraft:player") || !target.as(PlayerWrapper.class).getUuid().equals(event.getEntity().as(PlayerWrapper.class).getUuid())) {
            return;
        }
        // TODO: finish
    }
}

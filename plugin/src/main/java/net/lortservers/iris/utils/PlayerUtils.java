package net.lortservers.iris.utils;

import net.lortservers.iris.utils.material.MaterialUtils;
import org.screamingsandals.lib.event.player.SPlayerInteractEvent;
import org.screamingsandals.lib.player.PlayerWrapper;

/**
 * <p>Player utilities.</p>
 */
public class PlayerUtils {
    /**
     * <p>Checks if the player is blocking.</p>
     *
     * @param action the interact action
     * @param player the player
     * @return is the player blocking?
     */
    public static boolean isBlocking(SPlayerInteractEvent.Action action, PlayerWrapper player) {
        return (action == SPlayerInteractEvent.Action.RIGHT_CLICK_AIR || action == SPlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) && (MaterialUtils.hasPart(player.getPlayerInventory().getItemInMainHand(), "sword") || player.getPlayerInventory().getItemInOffHand().getMaterial().is("minecraft:shield"));
    }

    public static boolean isOnGround(PlayerWrapper player) {
        return !(Math.abs(player.asEntity().getVelocity().getY()) > 0) && !player.getLocation().remove(0, 1, 0).getBlock().isEmpty();
    }
}

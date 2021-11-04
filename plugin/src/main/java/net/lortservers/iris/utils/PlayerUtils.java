package net.lortservers.iris.utils;

import net.lortservers.iris.utils.material.MaterialUtils;
import org.screamingsandals.lib.event.player.SPlayerInteractEvent;
import org.screamingsandals.lib.player.PlayerWrapper;

/**
 * <p>Player utilities.</p>
 */
public final class PlayerUtils {
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

    public static boolean isHoldingMaterial(PlayerWrapper player, String mat) {
        return player.getPlayerInventory().getItemInMainHand().getMaterial().is(mat) || player.getPlayerInventory().getItemInOffHand().getMaterial().is(mat);
    }
}

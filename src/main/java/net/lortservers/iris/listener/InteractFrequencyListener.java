package net.lortservers.iris.listener;

import net.lortservers.iris.checks.interact.InteractFrequencyCheckA;
import net.lortservers.iris.checks.interact.block.BlockingFrequencyCheckA;
import net.lortservers.iris.config.Configurator;
import net.lortservers.iris.utils.MaterialUtils;
import net.lortservers.iris.utils.PlayerUtils;
import net.lortservers.iris.utils.Punisher;
import org.screamingsandals.lib.event.OnEvent;
import org.screamingsandals.lib.event.player.SPlayerBlockBreakEvent;
import org.screamingsandals.lib.event.player.SPlayerInteractEvent;
import org.screamingsandals.lib.event.player.SPlayerLeaveEvent;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.plugin.ServiceManager;
import org.screamingsandals.lib.utils.annotations.Service;

import java.util.*;

/**
 * <p>A class responsible for triggering interact frequency checks.</p>
 */
@Service(dependsOn = {
        Configurator.class,
        InteractFrequencyCheckA.class,
        BlockingFrequencyCheckA.class
})
public class InteractFrequencyListener {
    /**
     * <p>Left click interact actions.</p>
     */
    private static final List<SPlayerInteractEvent.Action> leftActions = Arrays.asList(SPlayerInteractEvent.Action.LEFT_CLICK_AIR, SPlayerInteractEvent.Action.LEFT_CLICK_BLOCK);
    /**
     * <p>Right click interact actions.</p>
     */
    private static final List<SPlayerInteractEvent.Action> rightActions = Arrays.asList(SPlayerInteractEvent.Action.RIGHT_CLICK_AIR, SPlayerInteractEvent.Action.RIGHT_CLICK_BLOCK);
    /**
     * <p>Left click CPS holder.</p>
     */
    private final Map<UUID, Integer> cpsLeft = new HashMap<>();
    /**
     * <p>Right click CPS holder.</p>
     */
    private final Map<UUID, Integer> cpsRight = new HashMap<>();
    /**
     * <p>Last block break time holder.</p>
     */
    private final Map<UUID, Long> lastBreak = new HashMap<>();
    /**
     * <p>Last hit time holder.</p>
     */
    private final Map<UUID, Long> lastHit = new HashMap<>();
    /**
     * <p>The configurator.</p>
     */
    private final Configurator configurator = ServiceManager.get(Configurator.class);

    /**
     * <p>Handles player leave events for cleaning up memory.</p>
     *
     * @param event the event
     */
    @OnEvent
    public void onPlayerLeave(SPlayerLeaveEvent event) {
        cpsLeft.remove(event.getPlayer().getUuid());
        cpsRight.remove(event.getPlayer().getUuid());
        lastBreak.remove(event.getPlayer().getUuid());
        lastHit.remove(event.getPlayer().getUuid());
    }

    /**
     * <p>Handles player block break events for check logic.</p>
     *
     * @param event the event
     */
    @OnEvent
    public void onPlayerBlockBreak(SPlayerBlockBreakEvent event) {
        cpsLeft.put(event.getPlayer().getUuid(), 0);
        lastBreak.put(event.getPlayer().getUuid(), System.currentTimeMillis());
    }

    /**
     * <p>Handles player interact events for check logic.</p>
     *
     * @param event the event
     */
    @OnEvent
    public void onPlayerInteract(SPlayerInteractEvent event) {
        if (event.getAction() == SPlayerInteractEvent.Action.PHYSICAL) {
            return;
        }
        final PlayerWrapper player = event.getPlayer();
        final long diff = System.currentTimeMillis() - lastBreak.getOrDefault(player.getUuid(), System.currentTimeMillis());
        if (diff <= 1500) {
            return;
        }
        if (leftActions.contains(event.getAction())) {
            cpsLeft.put(player.getUuid(), cpsLeft.getOrDefault(player.getUuid(), 0) + 1);
        }
        if (rightActions.contains(event.getAction())) {
            if (event.getBlockClicked() != null) {
                if (MaterialUtils.hasPart(event.getBlockClicked(), "fence_gate") || MaterialUtils.hasPart(event.getBlockClicked(), "button") || MaterialUtils.hasPart(event.getBlockClicked(), "door")) {
                    return;
                }
                if (event.getBlockClicked().getType().is("minecraft:daylight_detector") || event.getBlockClicked().getType().is("minecraft:repeater") || event.getBlockClicked().getType().is("minecraft:comparator") || event.getBlockClicked().getType().is("minecraft:lever")) {
                    return;
                }
                if (player.getPlayerInventory().getItemInMainHand().getMaterial().is("minecraft:fishing_rod") || player.getPlayerInventory().getItemInOffHand().getMaterial().is("minecraft:fishing_rod")) {
                    return;
                }
            }
            cpsRight.put(player.getUuid(), cpsRight.getOrDefault(player.getUuid(), 0) + 1);
        }
        if (!lastHit.containsKey(player.getUuid())) {
            lastHit.put(player.getUuid(), System.currentTimeMillis() - 1000);
        }
        if ((cpsLeft.get(player.getUuid()) >= configurator.getConfig().getInteractFrequencyAMaxCPS()) || (cpsRight.get(player.getUuid()) >= configurator.getConfig().getInteractFrequencyAMaxCPS())) {
            final InteractFrequencyCheckA a = ServiceManager.get(InteractFrequencyCheckA.class);
            if (!a.isOnCooldown(event.getPlayer()) && a.isEligibleForCheck(event.getPlayer())) {
                a.increaseVL(event.getPlayer(), 1);
                if (a.getVL(event.getPlayer()) >= a.getVLThreshold() && (System.currentTimeMillis() - lastBreak.getOrDefault(player.getUuid(), System.currentTimeMillis())) >= 1500) {
                    if (PlayerUtils.isBlocking(event.getAction(), player)) {
                        ServiceManager.get(Punisher.class).logWarn(event.getPlayer(), ServiceManager.get(BlockingFrequencyCheckA.class), "blocking too fast");
                    } else {
                        ServiceManager.get(Punisher.class).logWarn(event.getPlayer(), a, "seems to be using an autoclicker [LCPS: " + cpsLeft.get(player.getUuid()) + ", RCPS: " + cpsRight.get(player.getUuid()) + "]");
                    }
                }
                a.putCooldown(event.getPlayer());
            }
        }
    }
}

package net.lortservers.iris.listener;

import net.lortservers.iris.IrisPlugin;
import net.lortservers.iris.checks.interact.InteractFrequencyCheckA;
import net.lortservers.iris.checks.interact.block.BlockingFrequencyCheckA;
import net.lortservers.iris.config.ConfigurationManagerImpl;
import net.lortservers.iris.events.IrisCheckTriggerEvent;
import net.lortservers.iris.managers.ConfigurationManager;
import net.lortservers.iris.platform.EventManager;
import net.lortservers.iris.platform.events.IrisCheckTriggerEventImpl;
import net.lortservers.iris.utils.IntegerPair;
import net.lortservers.iris.utils.MaterialUtils;
import net.lortservers.iris.utils.PlayerUtils;
import net.lortservers.iris.utils.PunishmentManagerImpl;
import net.lortservers.iris.wrap.IntegerPairImpl;
import org.screamingsandals.lib.entity.EntityHuman;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.event.OnEvent;
import org.screamingsandals.lib.event.entity.SEntityDamageByEntityEvent;
import org.screamingsandals.lib.event.player.SPlayerBlockBreakEvent;
import org.screamingsandals.lib.event.player.SPlayerInteractEvent;
import org.screamingsandals.lib.event.player.SPlayerLeaveEvent;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.plugin.ServiceManager;
import org.screamingsandals.lib.tasker.Tasker;
import org.screamingsandals.lib.tasker.TaskerTime;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.annotations.methods.OnEnable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>A class responsible for triggering interact frequency checks.</p>
 */
@Service(dependsOn = {
        ConfigurationManagerImpl.class,
        PunishmentManagerImpl.class,
        InteractFrequencyCheckA.class,
        BlockingFrequencyCheckA.class
})
public class InteractFrequencyListener {
    /**
     * <p>Left click interact actions.</p>
     */
    private static final List<SPlayerInteractEvent.Action> leftActions = List.of(SPlayerInteractEvent.Action.LEFT_CLICK_AIR, SPlayerInteractEvent.Action.LEFT_CLICK_BLOCK);
    /**
     * <p>Right click interact actions.</p>
     */
    private static final List<SPlayerInteractEvent.Action> rightActions = List.of(SPlayerInteractEvent.Action.RIGHT_CLICK_AIR, SPlayerInteractEvent.Action.RIGHT_CLICK_BLOCK);
    /**
     * <p>Left click CPS holder.</p>
     */
    private final Map<UUID, IntegerPair> cps = new ConcurrentHashMap<>();
    /**
     * <p>Last block break time holder.</p>
     */
    private final Map<UUID, Long> lastBreak = new HashMap<>();

    public IntegerPair getCps(PlayerWrapper player) {
        if (!cps.containsKey(player.getUuid())) {
            cps.put(player.getUuid(), IntegerPairImpl.of(0, 0));
        }
        return cps.get(player.getUuid());
    }

    /**
     * <p>Initializes the listener.</p>
     */
    @OnEnable
    public void enable() {
        Tasker.build(() -> {
            for (PlayerWrapper player : PlayerMapper.getPlayers()) {
                getCps(player).modifyFull(0, 0);
            }
        }).repeat(22, TaskerTime.TICKS).start();
    }

    /**
     * <p>Handles player leave events for cleaning up memory.</p>
     *
     * @param event the event
     */
    @OnEvent
    public void onPlayerLeave(SPlayerLeaveEvent event) {
        cps.remove(event.getPlayer().getUuid());
        lastBreak.remove(event.getPlayer().getUuid());
    }

    /**
     * <p>Handles player block break events for check logic.</p>
     *
     * @param event the event
     */
    @OnEvent
    public void onPlayerBlockBreak(SPlayerBlockBreakEvent event) {
        getCps(event.getPlayer()).modifyFirst(0);
        lastBreak.put(event.getPlayer().getUuid(), System.currentTimeMillis());
    }

    /**
     * <p>Handles player damage events for check logic.</p>
     *
     * @param event the event
     */
    @OnEvent(priority = EventPriority.HIGHEST)
    public void onEntityDamageByEntity(SEntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof EntityHuman) {
            final PlayerWrapper attacker = event.getDamager().as(PlayerWrapper.class);
            final long diff = Math.abs(System.currentTimeMillis() - lastBreak.getOrDefault(attacker.getUuid(), 0L));
            if (diff <= 1500) {
                return;
            }
            getCps(attacker).incrementFirst(1);
            performCheck(attacker, SPlayerInteractEvent.Action.LEFT_CLICK_AIR);
        }
    }

    /**
     * <p>Handles player interact events for check logic.</p>
     *
     * @param event the event
     */
    @OnEvent(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(SPlayerInteractEvent event) {
        if (event.getAction() == SPlayerInteractEvent.Action.PHYSICAL) {
            return;
        }
        final PlayerWrapper player = event.getPlayer();
        final long diff = Math.abs(System.currentTimeMillis() - lastBreak.getOrDefault(player.getUuid(), 0L));
        if (diff <= 1500) {
            return;
        }
        if (leftActions.contains(event.getAction())) {
            getCps(player).incrementFirst(1);
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
            getCps(player).incrementSecond(1);
        }
        performCheck(player, event.getAction());
    }

    private void performCheck(PlayerWrapper player, SPlayerInteractEvent.Action action) {
        if (ConfigurationManager.getInstance().getValue("debug", Boolean.class).orElse(false)) {
            IrisPlugin.getInstance().getLogger().info("LCPS: " + getCps(player).first() + ", RCPS: " + getCps(player).second());
        }
        final InteractFrequencyCheckA a = ServiceManager.get(InteractFrequencyCheckA.class);
        if ((getCps(player).first() >= ConfigurationManager.getInstance().getValue("interactFrequencyAMaxCPS", Integer.class).orElse(16)) || (getCps(player).second() >= ConfigurationManager.getInstance().getValue("interactFrequencyAMaxCPS", Integer.class).orElse(16))) {
            if (a.isEligibleForCheck(player)) {
                final IrisCheckTriggerEvent evt1 = EventManager.fire(new IrisCheckTriggerEventImpl(player, a));
                if (!evt1.isCancelled()) {
                    a.increaseVL(player, 1);
                    if (a.getVL(player) >= a.getVLThreshold() && (System.currentTimeMillis() - lastBreak.getOrDefault(player.getUuid(), System.currentTimeMillis())) >= 1500) {
                        if (PlayerUtils.isBlocking(action, player)) {
                            final BlockingFrequencyCheckA a1 = ServiceManager.get(BlockingFrequencyCheckA.class);
                            if (a1.isEligibleForCheck(player)) {
                                final IrisCheckTriggerEvent evt = EventManager.fire(new IrisCheckTriggerEventImpl(player, a1));
                                if (!evt.isCancelled()) {
                                    a1.increaseVL(player, 1);
                                    if (a1.getVL(player) >= a1.getVLThreshold()) {
                                        ServiceManager.get(PunishmentManagerImpl.class).logWarn(player, a1, "blocking too fast [RCPS: " + getCps(player).second() + "]");
                                    }
                                }
                            }
                        } else {
                            ServiceManager.get(PunishmentManagerImpl.class).logWarn(player, a, "seems to be using an autoclicker [LCPS: " + getCps(player).first() + ", RCPS: " + getCps(player).second() + "]");
                        }
                    }
                }
            }
        } else {
            a.resetVL(player);
        }
    }
}

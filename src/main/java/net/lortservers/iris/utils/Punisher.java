package net.lortservers.iris.utils;

import net.kyori.adventure.text.Component;
import net.lortservers.iris.checks.Check;
import net.lortservers.iris.config.Configurator;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.screamingsandals.lib.event.OnEvent;
import org.screamingsandals.lib.event.player.SPlayerJoinEvent;
import org.screamingsandals.lib.event.player.SPlayerLeaveEvent;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.plugin.ServiceManager;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.annotations.methods.OnDisable;
import org.screamingsandals.lib.utils.annotations.methods.OnEnable;

import java.util.*;

@Service(dependsOn = {
        Configurator.class
})
public class Punisher {
    private final @NonNull List<UUID> subscribers = new ArrayList<>();

    public <T extends Check> void logWarn(PlayerWrapper player, T check) {
        final Component component = ServiceManager.get(Configurator.class).getMessages().getMessage(
                "failedCheck",
                Map.of("player", player.getName(), "name", check.getName(), "type", check.getType().name(), "vl", Integer.toString(check.getVL(player)))
        );
        subscribers.forEach(e -> PlayerMapper.wrapPlayer(e).sendMessage(component));
        // deez nuts
        if (ServiceManager.get(Configurator.class).getConfig().isDiscordWebhook()) {
            // nothing here, at least for next few mins
        }
    }

    @OnEnable
    public void enable() {
        PlayerMapper.getPlayers().forEach(this::subscribeAlerts);
    }

    @OnDisable
    public void disable() {
        PlayerMapper.getPlayers().forEach(this::unsubscribeAlerts);
    }

    @OnEvent
    public void onPlayerJoin(SPlayerJoinEvent event) {
        subscribeAlerts(event.getPlayer());
    }

    @OnEvent
    public void onPlayerLeave(SPlayerLeaveEvent event) {
        unsubscribeAlerts(event.getPlayer());
    }

    public void subscribeAlerts(PlayerWrapper player) {
        if (player.hasPermission("iris.alerts")) {
            subscribers.add(player.getUuid());
        }
    }

    public void unsubscribeAlerts(PlayerWrapper player) {
        if (player.hasPermission("iris.alerts")) {
            subscribers.remove(player.getUuid());
        }
    }

    public Optional<Boolean> toggleAlerts(PlayerWrapper player) {
        if (player.hasPermission("iris.alerts")) {
            if (subscribers.contains(player.getUuid())) {
                subscribers.remove(player.getUuid());
                return Optional.of(false);
            }
            subscribers.add(player.getUuid());
            return Optional.of(true);
        }
        return Optional.empty();
    }
}

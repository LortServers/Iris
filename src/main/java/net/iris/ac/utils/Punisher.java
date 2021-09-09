package net.iris.ac.utils;

import net.iris.ac.checks.Check;
import net.iris.ac.config.Configurator;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.screamingsandals.lib.event.OnEvent;
import org.screamingsandals.lib.event.player.SPlayerJoinEvent;
import org.screamingsandals.lib.event.player.SPlayerLeaveEvent;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.annotations.methods.OnDisable;
import org.screamingsandals.lib.utils.annotations.methods.OnEnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service(dependsOn = {
        Configurator.class
})
public class Punisher {
    private final List<UUID> subscribers = new ArrayList<>();

    public <T extends Check> void logWarn(PlayerWrapper player, T check) {
        final TextComponent component = Component.text()
                .append(Component.text("(", NamedTextColor.GRAY))
                .append(Component.text("!", NamedTextColor.RED))
                .append(Component.text(") ", NamedTextColor.GRAY))
                .append(Component.text(player.getName(), NamedTextColor.RED))
                .append(Component.text(" failed ", NamedTextColor.WHITE))
                .append(Component.text(check.getName() + " " + check.getType().name(), NamedTextColor.GOLD))
                .append(Component.text(" | ", NamedTextColor.GRAY))
                .append(Component.text("VL: ", NamedTextColor.BLUE))
                .append(Component.text(check.getVL(player)))
                .build();
        subscribers.forEach(e -> PlayerMapper.wrapPlayer(e).sendMessage(component));
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
}

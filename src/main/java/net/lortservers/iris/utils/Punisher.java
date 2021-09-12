package net.lortservers.iris.utils;

import me.zlataovce.hook.WebhookRequestDispatcher;
import me.zlataovce.hook.data.Embed;
import me.zlataovce.hook.requests.WebhookExecuteRequest;
import net.kyori.adventure.text.Component;
import net.lortservers.iris.IrisPlugin;
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

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.*;

@Service(dependsOn = {
        Configurator.class
})
public class Punisher {
    private final @NonNull List<UUID> subscribers = new ArrayList<>();

    public <T extends Check> void logWarn(PlayerWrapper player, T check) {
        final Component component = ServiceManager.get(Configurator.class).getMessage(
                "failedCheck",
                Map.of("player", player.getName(), "name", check.getName(), "type", check.getType().name(), "vl", Integer.toString(check.getVL(player)))
        );
        subscribers.forEach(e -> PlayerMapper.wrapPlayer(e).sendMessage(component));
        if (ServiceManager.get(Configurator.class).getConfig().isDiscordWebhook()) {
            final WebhookExecuteRequest request = WebhookExecuteRequest.builder()
                    .embed(
                            Embed.builder()
                                    .color(0xFFA500)
                                    .thumbnail(
                                            Embed.Media.builder()
                                                    .url("https://mc-heads.net/avatar/" + player.getUuid())
                                                    .build()
                                    )
                                    .title("Violation")
                                    .description(player.getName())
                                    .field(
                                            Embed.Field.builder()
                                                    .name("Check")
                                                    .value(check.getName() + " " + check.getType().name())
                                                    .build()
                                    )
                                    .field(
                                            Embed.Field.builder()
                                                    .name("VL")
                                                    .value(check.getVL(player) + "/" + check.getVLThreshold())
                                                    .build()
                                    )
                                    .field(
                                            Embed.Field.builder()
                                                    .name("Ping")
                                                    .value(player.getPing() + "ms")
                                                    .build()
                                    )
                                    .field(
                                            Embed.Field.builder()
                                                    .name("Location")
                                                    .value(
                                                            "X: " + player.getLocation().getX() +
                                                                    "\nY: " + player.getLocation().getY() +
                                                                    "\nZ: " + player.getLocation().getZ() +
                                                                    "\nYaw: " + player.getLocation().getYaw() +
                                                                    "\nPitch: " + player.getLocation().getPitch() +
                                                                    "\nWorld: " + player.getLocation().getWorld().getName()
                                                    )
                                                    .build()
                                    )
                                    .build()
                    )
                    .build();
            try {
                WebhookRequestDispatcher.execute(
                        ServiceManager.get(Configurator.class).getConfig().getWebhookUrl(),
                        request
                );
            } catch (MalformedURLException | URISyntaxException e) {
                IrisPlugin.getInstance().getLogger().error("Malformed Discord webhook URL!");
            }
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

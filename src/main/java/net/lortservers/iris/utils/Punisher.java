package net.lortservers.iris.utils;

import me.zlataovce.hook.WebhookRequestDispatcher;
import me.zlataovce.hook.data.Embed;
import me.zlataovce.hook.requests.WebhookExecuteRequest;
import net.kyori.adventure.text.Component;
import net.lortservers.iris.IrisPlugin;
import net.lortservers.iris.checks.Check;
import net.lortservers.iris.config.Configurator;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>Class responsible for dispatching failed messages and punishing players.</p>
 */
@Service(dependsOn = {
        Configurator.class
})
public class Punisher {
    /**
     * <p>Alert subscribers.</p>
     */
    private final @NonNull List<UUID> subscribers = new ArrayList<>();

    /**
     * <p>Sends a failed message to alert subscribers.</p>
     *
     * @param player the player
     * @param check the check class
     * @param <T> the check class type
     */
    public <T extends Check> void logWarn(PlayerWrapper player, T check) {
        logWarn(player, check, null);
    }

    /**
     * <p>Sends a failed message to alert subscribers.</p>
     *
     * @param player the player
     * @param check the check class
     * @param <T> the check class type
     * @param info additional info to log
     */
    public <T extends Check> void logWarn(PlayerWrapper player, T check, @Nullable String info) {
        Component component;
        if (info == null) {
            component = ServiceManager.get(Configurator.class).getMessage(
                    "shortFailedCheck",
                    Map.of("player", player.getName(), "name", check.getName(), "type", check.getType().name(), "vl", Integer.toString(check.getVL(player)))
            );
        } else {
            component = ServiceManager.get(Configurator.class).getMessage(
                    "failedCheck",
                    Map.of("player", player.getName(), "name", check.getName(), "type", check.getType().name(), "vl", Integer.toString(check.getVL(player)), "info", info)
            );
        }
        subscribers.forEach(e -> PlayerMapper.wrapPlayer(e).sendMessage(component));
        PlayerMapper.getConsoleSender().sendMessage(component);
        if (ServiceManager.get(Configurator.class).getConfig().isDiscordWebhook()) {
            final Optional<ProtocolUtils.Protocol> proto = ProtocolUtils.getProtocol(player.getProtocolVersion());
            final String protocolString = (proto.isPresent()) ? proto.orElseThrow().getVersion() + " (" + proto.orElseThrow().getMinecraftVersion() + ")" : "Unknown";
            final Embed.EmbedBuilder embed = Embed.builder()
                    .thumbnail(
                            Embed.Media.builder()
                                    .url("https://mc-heads.net/head/" + player.getUuid())
                                    .build()
                    )
                    .title("Iris check violation")
                    .description("```md\n<" + player.getName() + " failed> [" + check.getName() + "](Type: " + check.getType().name() + ") (VL: " + check.getVL(player) + ")\n```")
                    .field(
                            Embed.Field.builder()
                                    .name("Location")
                                    .value("`" + Stream.of(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ()).map(e -> Double.toString(e)).collect(Collectors.joining(", ")) + " - " + player.getLocation().getWorld().getName() + "`")
                                    .build()
                    )
                    .field(
                            Embed.Field.builder()
                                    .name("Player details")
                                    .value("```yaml\nPing: " + player.getPing() + "ms\n" + "Protocol version: " + protocolString + "```")
                                    .build()
                    )
                    .timestamp(new Date(System.currentTimeMillis()));
            if (info != null) {
                embed.field(
                        Embed.Field.builder()
                                .name("Addítional information")
                                .value("`" + info + "`")
                                .build()
                );
            }
            try {
                WebhookRequestDispatcher.execute(
                        ServiceManager.get(Configurator.class).getConfig().getWebhookUrl(),
                        WebhookExecuteRequest.builder().embed(embed.build()).build()
                );
            } catch (MalformedURLException | URISyntaxException e) {
                IrisPlugin.getInstance().getLogger().error("Malformed Discord webhook URL!");
            }
        }
    }

    /**
     * <p>Subscribes all eligible players to alerts.</p>
     */
    @OnEnable
    public void enable() {
        PlayerMapper.getPlayers().forEach(this::subscribeAlerts);
    }

    /**
     * <p>Unsubscribes all eligible players from alerts.</p>
     */
    @OnDisable
    public void disable() {
        PlayerMapper.getPlayers().forEach(this::unsubscribeAlerts);
    }

    /**
     * <p>Subscribes the joined player to alerts if he's eligible.</p>
     *
     * @param event the event
     */
    @OnEvent
    public void onPlayerJoin(SPlayerJoinEvent event) {
        subscribeAlerts(event.getPlayer());
    }

    /**
     * <p>Unsubscribes the leaving player from alerts if he's subscribed.</p>
     *
     * @param event the event
     */
    @OnEvent
    public void onPlayerLeave(SPlayerLeaveEvent event) {
        unsubscribeAlerts(event.getPlayer());
    }

    /**
     * <p>Subscribes the player to alerts.</p>
     *
     * @param player the player
     */
    public void subscribeAlerts(PlayerWrapper player) {
        if (player.hasPermission("iris.alerts")) {
            subscribers.add(player.getUuid());
        }
    }

    /**
     * <p>Unsubscribes the player from alerts.</p>
     *
     * @param player the player
     */
    public void unsubscribeAlerts(PlayerWrapper player) {
        if (player.hasPermission("iris.alerts")) {
            subscribers.remove(player.getUuid());
        }
    }

    /**
     * <p>Toggles alerts for the player.</p>
     *
     * @param player the player
     * @return the current alert status, empty if the player doesn't have the permission
     */
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

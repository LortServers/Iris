package net.lortservers.iris.utils;

import me.zlataovce.hook.WebhookRequestDispatcher;
import me.zlataovce.hook.data.Embed;
import me.zlataovce.hook.requests.WebhookExecuteRequest;
import net.kyori.adventure.text.Component;
import net.lortservers.iris.IrisPlugin;
import net.lortservers.iris.checks.Check;
import net.lortservers.iris.config.ConfigurationManagerImpl;
import net.lortservers.iris.managers.ConfigurationManager;
import net.lortservers.iris.managers.PunishmentManager;
import net.lortservers.iris.utils.profiles.PlayerProfile;
import net.lortservers.iris.utils.profiles.PlayerProfileManager;
import net.lortservers.iris.utils.tasks.AsyncExecutor;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.screamingsandals.lib.packet.PacketMapper;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.annotations.Service;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.http.HttpResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>Class responsible for dispatching failed messages and punishing players.</p>
 */
@Service(dependsOn = {
        ConfigurationManagerImpl.class,
        PlayerProfileManager.class
})
public class PunishmentManagerImpl implements PunishmentManager {
    private final CooldownMapping webhookCooldown = new CooldownMapping(10000);

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
        final String loc = Stream.of(player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ()).map(e -> Integer.toString(e)).collect(Collectors.joining(", ")) + ", " + player.getLocation().getWorld().getName();
        Component component;
        if (info == null) {
            component = ConfigurationManager.getInstance().getMessage(
                    "shortFailedCheck",
                    Map.of("player", player.getName(), "check", check.getName(), "type", check.getType().name(), "vl", Integer.toString(check.getVL(player)), "ping", Integer.toString(player.getPing()), "loc", loc)
            );
        } else {
            component = ConfigurationManager.getInstance().getMessage(
                    "failedCheck",
                    Map.of("player", player.getName(), "check", check.getName(), "type", check.getType().name(), "vl", Integer.toString(check.getVL(player)), "info", info, "ping", Integer.toString(player.getPing()), "loc", loc)
            );
        }
        getSubscribers().forEach(e -> e.sendMessage(component));
        PlayerMapper.getConsoleSender().sendMessage(component);
        if (!ConfigurationManager.getInstance().getValue("webhookUrl", String.class).orElse("").equals("") && !webhookCooldown.isOnCooldown()) {
            webhookCooldown.putCooldown();
            final Optional<Protocol> proto = ProtocolUtils.getProtocol(PacketMapper.getProtocolVersion(player));
            final String protocolString = (proto.isPresent()) ? proto.orElseThrow().getVersion() + " (" + proto.orElseThrow().getMinecraftVersion() + ")" : "Unknown";
            final Embed.EmbedBuilder embed = Embed.builder()
                    .thumbnail(
                            Embed.Media.builder()
                                    .url("https://mc-heads.net/head/" + player.getUuid())
                                    .build()
                    )
                    .title("Check violation")
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
                                    .value("```yaml\nPing: " + player.getPing() + "ms\nProtocol version: " + protocolString + "```")
                                    .build()
                    )
                    .timestamp(new Date(System.currentTimeMillis()));
            if (info != null) {
                embed.field(
                        Embed.Field.builder()
                                .name("Additional information")
                                .value("`" + info + "`")
                                .build()
                );
            }
            AsyncExecutor.executeTask(() -> {
                try {
                    final HttpResponse<String> response = WebhookRequestDispatcher.execute(
                            ConfigurationManager.getInstance().getValue("webhookUrl", String.class).orElseThrow(),
                            WebhookExecuteRequest.builder()
                                    .username("Iris")
                                    .avatarUrl(ConfigurationManager.getInstance().getValue("webhookAvatar", String.class).orElse(null))
                                    .embed(embed.build())
                                    .build()
                    );
                    if (ConfigurationManager.getInstance().getValue("debug", Boolean.class).orElse(false)) {
                        IrisPlugin.getInstance().getLogger().info(response.body());
                    }
                } catch (MalformedURLException | URISyntaxException e) {
                    IrisPlugin.getInstance().getLogger().error("Malformed Discord webhook URL!");
                }
            });
        }
    }

    @Override
    public List<PlayerWrapper> getSubscribers() {
        return PlayerProfileManager.all().stream().filter(PlayerProfile::isAlertSubscriber).map(PlayerProfile::toPlayer).toList();
    }

    @Override
    public boolean isSubscribed(PlayerWrapper player) {
        return PlayerProfileManager.ofPlayer(player).isAlertSubscriber();
    }

    /**
     * <p>Toggles alerts for the player.</p>
     *
     * @param player the player
     * @return the current alert status
     */
    @Override
    public boolean toggleAlerts(PlayerWrapper player) {
        if (PunishmentManager.canSubscribe(player)) {
            final PlayerProfile profile = PlayerProfileManager.ofPlayer(player);
            final boolean now = !profile.isAlertSubscriber();
            profile.setAlertSubscriber(now);
            return now;
        }
        return false;
    }
}

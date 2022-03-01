package net.lortservers.iris.utils;

import com.google.common.collect.ImmutableMap;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.lortservers.iris.IrisPlugin;
import net.lortservers.iris.api.checks.Check;
import net.lortservers.iris.api.events.IrisCheckMessageSendEvent;
import net.lortservers.iris.api.managers.ConfigurationManager;
import net.lortservers.iris.api.managers.PunishmentManager;
import net.lortservers.iris.api.managers.TranslationManager;
import net.lortservers.iris.config.ConfigurationManagerImpl;
import net.lortservers.iris.config.TranslationManagerImpl;
import net.lortservers.iris.platform.EventManager;
import net.lortservers.iris.platform.events.IrisCheckMessageSendEventImpl;
import net.lortservers.iris.utils.profiles.EphemeralPlayerProfile;
import net.lortservers.iris.utils.profiles.PlayerProfileManager;
import net.lortservers.iris.utils.protocol.Protocol;
import net.lortservers.iris.utils.protocol.ProtocolUtils;
import net.lortservers.iris.utils.webhooks.WebhookDispatcher;
import net.lortservers.iris.utils.webhooks.WebhookExecuteRequest;
import net.lortservers.iris.utils.webhooks.data.Embed;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.screamingsandals.lib.Server;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.event.OnEvent;
import org.screamingsandals.lib.event.player.SPlayerJoinEvent;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.annotations.methods.OnEnable;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>Class responsible for dispatching failed messages and punishing players.</p>
 */
@Service(dependsOn = {
        ConfigurationManagerImpl.class,
        TranslationManagerImpl.class,
        PlayerProfileManager.class
})
public class PunishmentManagerImpl implements PunishmentManager {
    private final CooldownMapping webhookCooldown = new CooldownMapping(10000);
    private ConfigurationManager configurationManager;
    private TranslationManager translationManager;

    @OnEnable
    public void enable(ConfigurationManagerImpl configurationManager, TranslationManagerImpl translationManager) {
        this.configurationManager = configurationManager;
        this.translationManager = translationManager;
    }

    /**
     * <p>Sends a failed message to alert subscribers.</p>
     *
     * @param player the player
     * @param check the check class
     * @param <T> the check class type
     */
    public <T extends Check> void log(PlayerWrapper player, T check) {
        log(player, check, null);
    }

    /**
     * <p>Sends a failed message to alert subscribers.</p>
     *
     * @param player the player
     * @param check the check class
     * @param <T> the check class type
     * @param info additional info to log
     */
    public <T extends Check> void log(PlayerWrapper player, T check, @Nullable String info) {
        final ImmutableMap.Builder<String, String> placeholders = new ImmutableMap.Builder<String, String>()
                .put("player", player.getName())
                .put("check", check.getName())
                .put("type", check.getType().name())
                .put("vl", Integer.toString(check.getVL(player)))
                .put("ping", Integer.toString(player.getPing()))
                .put("loc", Stream.of(player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ()).map(e -> Integer.toString(e)).collect(Collectors.joining(", ")) + ", " + player.getLocation().getWorld().getName());
        String messageId;
        if (info != null) {
            placeholders.put("info", info);
            messageId = "failedCheck";
        } else {
            messageId = "shortFailedCheck";
        }
        messageForSubscribers(messageId, placeholders.build()).thenAccept(messages -> Server.runSynchronously(() -> {
            final IrisCheckMessageSendEvent evt = EventManager.fire(new IrisCheckMessageSendEventImpl(messages));
            if (!evt.isCancelled()) {
                IrisPlugin.THREAD_POOL.submit(() -> {
                    messages.forEach(Audience::sendMessage);
                    PlayerMapper.getConsoleSender().sendMessage(translationManager.getMessage(messageId, placeholders.build()));
                });
            }
        }));
        if (configurationManager.getValue("webhookUrl", String.class).orElse(null) != null && !webhookCooldown.isOnCooldown()) {
            webhookCooldown.putCooldown();
            final Protocol proto = ProtocolUtils.getPlayerProtocol(player);
            final String protocolString = proto.getVersion() + " (" + proto.getMinecraftVersion() + ")";
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
                                    .value("```yaml\nPing: " + player.getPing() + "ms\nProtocol version: " + protocolString + "\n```")
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
            WebhookDispatcher.execute(
                    WebhookExecuteRequest.builder()
                            .username("Iris")
                            .avatarUrl(configurationManager.getValue("webhookAvatar", String.class).orElse(null))
                            .embed(embed.build())
                            .build()
            ).thenAccept(e -> {
                if (configurationManager.getValue("debug", Boolean.class).orElse(false)) {
                    IrisPlugin.getInstance().getLogger().info((e != null) ? e.body() : "");
                }
            });
        }
    }

    private CompletableFuture<Map<PlayerWrapper, Component>> messageForSubscribers(String id, Map<String, String> placeholders) {
        return CompletableFuture.supplyAsync(() -> {
            final ImmutableMap.Builder<PlayerWrapper, Component> messages = ImmutableMap.builder();
            getSubscribers().forEach(player -> messages.put(player, translationManager.getMessage(id, placeholders)));
            return messages.build();
        }, IrisPlugin.THREAD_POOL);
    }

    @Override
    public void kick(PlayerWrapper player, String message) {
        player.kick(translationManager.getMessage("banMessage", Map.of("message", message), player.getLocale()));
    }

    // TODO: ban event
    @Override
    public void ban(PlayerWrapper player, String message) {
        PlayerProfileManager.ofPersistent(player).thenAcceptAsync(e -> {
            try (e) {
                e.setBanMessage(message);
            }
            kick(player, message);
        });
    }

    @Override
    public List<PlayerWrapper> getSubscribers() {
        return PlayerProfileManager.allEphemeral().stream().filter(EphemeralPlayerProfile::isAlertSubscriber).map(EphemeralPlayerProfile::toPlayer).map(Optional::orElseThrow).toList();
    }

    @Override
    public boolean isSubscribed(PlayerWrapper player) {
        return PlayerProfileManager.ofEphemeral(player).isAlertSubscriber();
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
            final EphemeralPlayerProfile profile = PlayerProfileManager.ofEphemeral(player);
            final boolean now = !profile.isAlertSubscriber();
            profile.setAlertSubscriber(now);
            return now;
        }
        return false;
    }

    @OnEvent(priority = EventPriority.HIGH)
    public void onPlayerJoin(SPlayerJoinEvent event) {
        PlayerProfileManager.ofPersistent(event.player()).thenAccept(e -> {
            if (e.isBanned()) {
                Server.runSynchronously(() -> kick(event.player(), e.getBanMessage()));
            }
        });
    }
}

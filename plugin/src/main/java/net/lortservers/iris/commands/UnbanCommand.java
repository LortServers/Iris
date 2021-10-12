package net.lortservers.iris.commands;

import cloud.commandframework.Command;
import cloud.commandframework.CommandManager;
import net.lortservers.iris.api.managers.TranslationManager;
import net.lortservers.iris.utils.PunishmentManagerImpl;
import net.lortservers.iris.utils.profiles.PlayerProfileManager;
import org.screamingsandals.lib.Server;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.sender.CommandSenderWrapper;
import org.screamingsandals.lib.sender.permissions.SimplePermission;
import org.screamingsandals.lib.utils.annotations.Service;

import java.util.Map;
import java.util.Optional;

@Service(dependsOn = {
        PlayerProfileManager.class,
        PunishmentManagerImpl.class
})
public class UnbanCommand extends BaseCommand {
    public UnbanCommand() {
        super("unban", SimplePermission.of("iris.unban"), true);
    }

    @Override
    protected void construct(Command.Builder<CommandSenderWrapper> commandSenderWrapperBuilder, CommandManager<CommandSenderWrapper> manager) {
        manager.command(
                commandSenderWrapperBuilder
                        .argument(
                                manager
                                        .argumentBuilder(String.class, "player")
                                        .withSuggestionsProvider((c, s) ->
                                                Server.getConnectedPlayers().stream().map(PlayerWrapper::getName).toList()
                                        )
                        )
                        .handler(commandContext -> {
                            final Optional<PlayerWrapper> player = PlayerMapper.getPlayer((String) commandContext.get("player"));
                            if (player.isEmpty()) {
                                commandContext.getSender().sendMessage(
                                        TranslationManager.getInstance().getMessage("invalidPlayer", commandContext.getSender().getLocale())
                                );
                                return;
                            }
                            PlayerProfileManager.ofPersistent(player.orElseThrow())
                                            .thenAcceptAsync(e -> {
                                                try (e) {
                                                    e.setBanMessage(null);
                                                }
                                                commandContext.getSender().sendMessage(
                                                        TranslationManager.getInstance().getMessage("unbanCommandSuccess", Map.of("player", player.orElseThrow().getName()), commandContext.getSender().getLocale())
                                                );
                                            });
                        })
        );
    }
}

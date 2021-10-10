package net.lortservers.iris.commands;

import cloud.commandframework.Command;
import cloud.commandframework.CommandManager;
import cloud.commandframework.arguments.standard.StringArgument;
import net.lortservers.iris.api.managers.ConfigurationManager;
import net.lortservers.iris.api.managers.PunishmentManager;
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
public class BanCommand extends BaseCommand {
    public BanCommand() {
        super("ban", SimplePermission.of("iris.ban"), true);
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
                        .argument(StringArgument.optional("reason", ConfigurationManager.getInstance().getRawMessage("banMessageCheating")))
                        .handler(commandContext -> {
                            final Optional<PlayerWrapper> player = PlayerMapper.getPlayer((String) commandContext.get("player"));
                            if (player.isEmpty()) {
                                commandContext.getSender().sendMessage(
                                        ConfigurationManager.getInstance().getMessage("invalidPlayer")
                                );
                                return;
                            }
                            PunishmentManager.getInstance().ban(player.orElseThrow(), commandContext.get("reason"));
                            commandContext.getSender().sendMessage(
                                    ConfigurationManager.getInstance().getMessage("banCommandSuccess", Map.of("player", player.orElseThrow().getName()))
                            );
                        })
        );
    }
}

package net.lortservers.iris.commands;

import cloud.commandframework.Command;
import cloud.commandframework.CommandManager;
import cloud.commandframework.arguments.standard.BooleanArgument;
import net.lortservers.iris.api.managers.ConfigurationManager;
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
        PlayerProfileManager.class
})
public class JudgementDaySetCommand extends BaseCommand {
    public JudgementDaySetCommand() {
        super("jd", SimplePermission.of("iris.judgementday.set"), true);
    }

    @Override
    protected void construct(Command.Builder<CommandSenderWrapper> commandSenderWrapperBuilder, CommandManager<CommandSenderWrapper> manager) {
        manager.command(
                commandSenderWrapperBuilder
                        .literal("set")
                        .argument(manager
                                .argumentBuilder(String.class, "player")
                                .withSuggestionsProvider((c, s) ->
                                        Server.getConnectedPlayers().stream().map(PlayerWrapper::getName).toList()
                                )
                        )
                        .argument(BooleanArgument.of("state"))
                        .handler(commandContext -> {
                            final Optional<PlayerWrapper> player = PlayerMapper.getPlayer((String) commandContext.get("player"));
                            if (player.isEmpty()) {
                                commandContext.getSender().sendMessage(
                                        ConfigurationManager.getInstance().getMessage("invalidPlayer")
                                );
                                return;
                            }
                            PlayerProfileManager.modify(player.orElseThrow(), e -> {
                                e.setJudgementDay(commandContext.get("state"));
                                return e;
                            }).thenAccept(e -> commandContext.getSender().sendMessage(
                                    ConfigurationManager.getInstance().getMessage(
                                            "judgementDaySet",
                                            Map.of(
                                                    "player", player.orElseThrow().getName(),
                                                    "status", Boolean.toString(commandContext.get("state"))
                                            )
                                    )
                            ));
                        })
        );
    }
}

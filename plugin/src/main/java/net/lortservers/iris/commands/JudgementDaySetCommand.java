package net.lortservers.iris.commands;

import cloud.commandframework.Command;
import cloud.commandframework.CommandManager;
import cloud.commandframework.arguments.standard.BooleanArgument;
import cloud.commandframework.bukkit.parsers.PlayerArgument;
import net.lortservers.iris.api.managers.ConfigurationManager;
import net.lortservers.iris.utils.profiles.PlayerProfileManager;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.sender.CommandSenderWrapper;
import org.screamingsandals.lib.sender.permissions.SimplePermission;
import org.screamingsandals.lib.utils.annotations.Service;

import java.util.Map;

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
                        .argument(PlayerArgument.of("player"))
                        .argument(BooleanArgument.of("state"))
                        .handler(commandContext -> PlayerProfileManager.modify(commandContext.get("player"), e -> {
                            e.setJudgementDay(commandContext.get("state"));
                            return e;
                        }).thenAccept(e -> commandContext.getSender().sendMessage(
                                ConfigurationManager.getInstance().getMessage(
                                        "judgementDaySet",
                                        Map.of(
                                                "player", ((PlayerWrapper) commandContext.get("player")).getName(),
                                                "status", Boolean.toString(commandContext.get("state"))
                                        )
                                )
                        )))
        );
    }
}

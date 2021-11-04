package net.lortservers.iris.commands;

import cloud.commandframework.Command;
import cloud.commandframework.CommandManager;
import cloud.commandframework.arguments.standard.BooleanArgument;
import net.lortservers.iris.api.managers.TranslationManager;
import net.lortservers.iris.config.TranslationManagerImpl;
import net.lortservers.iris.utils.PlayerWrapperArgument;
import net.lortservers.iris.utils.profiles.PlayerProfileManager;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.sender.CommandSenderWrapper;
import org.screamingsandals.lib.sender.permissions.SimplePermission;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.annotations.methods.OnEnable;

import java.util.Map;

@Service(dependsOn = {
        TranslationManagerImpl.class,
        PlayerProfileManager.class
})
public class JudgementDaySetCommand extends BaseCommand {
    private TranslationManager translationManager;

    public JudgementDaySetCommand() {
        super("jd", SimplePermission.of("iris.judgementday.set"), true);
    }

    @OnEnable
    public void enable(TranslationManagerImpl translationManager) {
        this.translationManager = translationManager;
    }

    @Override
    protected void construct(Command.Builder<CommandSenderWrapper> commandSenderWrapperBuilder, CommandManager<CommandSenderWrapper> manager) {
        manager.command(
                commandSenderWrapperBuilder
                        .literal("set")
                        .argument(PlayerWrapperArgument.of("player"))
                        .argument(BooleanArgument.of("state"))
                        .handler(commandContext -> {
                            final PlayerWrapper player = commandContext.get("player");
                            PlayerProfileManager.ofPersistent(player)
                                            .thenAcceptAsync(e -> {
                                                try (e) {
                                                    e.setJudgementDay(commandContext.get("state"));
                                                }
                                                commandContext.getSender().sendMessage(
                                                        translationManager.getMessage(
                                                                "judgementDaySet",
                                                                Map.of(
                                                                        "player", player.getName(),
                                                                        "status", Boolean.toString(commandContext.get("state"))
                                                                ),
                                                                commandContext.getSender().getLocale()
                                                        )
                                                );
                                            });
                        })
        );
    }
}

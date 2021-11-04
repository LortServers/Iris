package net.lortservers.iris.commands;

import cloud.commandframework.Command;
import cloud.commandframework.CommandManager;
import cloud.commandframework.arguments.standard.StringArgument;
import net.lortservers.iris.api.managers.PunishmentManager;
import net.lortservers.iris.api.managers.TranslationManager;
import net.lortservers.iris.config.TranslationManagerImpl;
import net.lortservers.iris.utils.PlayerWrapperArgument;
import net.lortservers.iris.utils.PunishmentManagerImpl;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.sender.CommandSenderWrapper;
import org.screamingsandals.lib.sender.permissions.SimplePermission;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.annotations.methods.OnEnable;

import java.util.Map;

@Service(dependsOn = {
        PunishmentManagerImpl.class,
        TranslationManagerImpl.class
})
public class BanCommand extends BaseCommand {
    private PunishmentManager punishmentManager;
    private TranslationManager translationManager;

    public BanCommand() {
        super("ban", SimplePermission.of("iris.ban"), true);
    }

    @OnEnable
    public void enable(TranslationManagerImpl translationManager, PunishmentManagerImpl punishmentManager) {
        this.translationManager = translationManager;
        this.punishmentManager = punishmentManager;
    }

    @Override
    protected void construct(Command.Builder<CommandSenderWrapper> commandSenderWrapperBuilder, CommandManager<CommandSenderWrapper> manager) {
        manager.command(
                commandSenderWrapperBuilder
                        .argument(PlayerWrapperArgument.of("player"))
                        .argument(StringArgument.optional("reason", translationManager.getRawMessage("banMessageCheating")))
                        .handler(commandContext -> {
                            final PlayerWrapper player = commandContext.get("player");
                            punishmentManager.ban(player, commandContext.get("reason"));
                            commandContext.getSender().sendMessage(
                                    translationManager.getMessage("banCommandSuccess", Map.of("player", player.getName()), commandContext.getSender().getLocale())
                            );
                        })
        );
    }
}

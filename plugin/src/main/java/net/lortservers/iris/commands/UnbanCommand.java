package net.lortservers.iris.commands;

import cloud.commandframework.Command;
import cloud.commandframework.CommandManager;
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
        PlayerProfileManager.class,
        TranslationManagerImpl.class
})
public class UnbanCommand extends BaseCommand {
    private TranslationManager translationManager;

    public UnbanCommand() {
        super("unban", SimplePermission.of("iris.unban"), true);
    }

    @OnEnable
    public void enable(TranslationManagerImpl translationManager) {
        this.translationManager = translationManager;
    }

    @Override
    protected void construct(Command.Builder<CommandSenderWrapper> commandSenderWrapperBuilder, CommandManager<CommandSenderWrapper> manager) {
        manager.command(
                commandSenderWrapperBuilder
                        .argument(PlayerWrapperArgument.of("player"))
                        .handler(commandContext -> {
                            final PlayerWrapper player = commandContext.get("player");
                            PlayerProfileManager.ofPersistent(player)
                                            .thenAcceptAsync(e -> {
                                                try (e) {
                                                    e.setBanMessage(null);
                                                }
                                                commandContext.getSender().sendMessage(
                                                        translationManager.getMessage("unbanCommandSuccess", Map.of("player", player.getName()), commandContext.getSender().getLocale())
                                                );
                                            });
                        })
        );
    }
}

package net.lortservers.iris.commands;

import cloud.commandframework.Command;
import cloud.commandframework.CommandManager;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.lortservers.iris.config.Configurator;
import net.lortservers.iris.utils.Punisher;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.plugin.ServiceManager;
import org.screamingsandals.lib.sender.CommandSenderWrapper;
import org.screamingsandals.lib.sender.permissions.SimplePermission;
import org.screamingsandals.lib.utils.annotations.Service;

import java.util.Map;
import java.util.Optional;

@Service(dependsOn = {
        Punisher.class,
        Configurator.class
})
public class AlertsCommand extends BaseCommand {
    private static final Map<Boolean, String> BOOL_ABBR = Map.of(true, "on", false, "off");

    public AlertsCommand() {
        super("alerts", SimplePermission.of("iris.alerts"), false);
    }

    @Override
    protected void construct(Command.Builder<CommandSenderWrapper> commandSenderWrapperBuilder, CommandManager<CommandSenderWrapper> manager) {
        manager.command(
                commandSenderWrapperBuilder
                        .handler(commandContext -> {
                            final Optional<Boolean> now = ServiceManager.get(Punisher.class).toggleAlerts(commandContext.getSender().as(PlayerWrapper.class));

                            if (now.isEmpty()) {
                                commandContext.getSender().sendMessage(
                                        MiniMessage.get().parse(
                                                ServiceManager.get(Configurator.class).getConfig().getPrefix() +
                                                        "<color:red>No permission!"
                                        )
                                );
                                return;
                            }
                            commandContext.getSender().sendMessage(
                                    MiniMessage.get().parse(
                                            ServiceManager.get(Configurator.class).getConfig().getPrefix() +
                                                    "<color:yellow>Turned alerts <color:green>" + BOOL_ABBR.get(now.orElseThrow()) + "<color:yellow>!"
                                    )
                            );
                        })
        );
    }
}

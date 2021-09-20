package net.lortservers.iris.commands;

import cloud.commandframework.Command;
import cloud.commandframework.CommandManager;
import net.lortservers.iris.managers.ConfigurationManager;
import net.lortservers.iris.utils.PunishmentManagerImpl;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.plugin.ServiceManager;
import org.screamingsandals.lib.sender.CommandSenderWrapper;
import org.screamingsandals.lib.sender.permissions.SimplePermission;
import org.screamingsandals.lib.utils.annotations.Service;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

/**
 * <p>A class representing the alerts command.</p>
 */
@Service(dependsOn = {
        PunishmentManagerImpl.class
})
public class AlertsCommand extends BaseCommand {
    /**
     * <p>Boolean to on/off abbreviations.</p>
     */
    private static final Map<Boolean, String> BOOL_ABBR = Map.of(true, "on", false, "off");

    /**
     * <p>Constructs the command.</p>
     */
    public AlertsCommand() {
        super("alerts", SimplePermission.of("iris.alerts"), false);
    }

    /**
     * <p>Constructs the command behavior.</p>
     *
     * @param commandSenderWrapperBuilder the command builder
     * @param manager the command manager
     */
    @Override
    protected void construct(Command.Builder<CommandSenderWrapper> commandSenderWrapperBuilder, CommandManager<CommandSenderWrapper> manager) {
        manager.command(
                commandSenderWrapperBuilder
                        .handler(commandContext -> {
                            final Optional<Boolean> now = ServiceManager.get(PunishmentManagerImpl.class).toggleAlerts(commandContext.getSender().as(PlayerWrapper.class));

                            if (now.isEmpty()) {
                                commandContext.getSender().sendMessage(
                                        ConfigurationManager.getInstance().getMessage("noPermission")
                                );
                                return;
                            }
                            commandContext.getSender().sendMessage(
                                    ConfigurationManager.getInstance().getMessage("alertsToggle", Collections.singletonMap("status", BOOL_ABBR.get(now.orElseThrow())))
                            );
                        })
        );
    }
}

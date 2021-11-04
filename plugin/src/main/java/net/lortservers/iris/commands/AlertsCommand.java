package net.lortservers.iris.commands;

import cloud.commandframework.Command;
import cloud.commandframework.CommandManager;
import net.lortservers.iris.api.managers.PunishmentManager;
import net.lortservers.iris.api.managers.TranslationManager;
import net.lortservers.iris.config.TranslationManagerImpl;
import net.lortservers.iris.utils.PunishmentManagerImpl;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.sender.CommandSenderWrapper;
import org.screamingsandals.lib.sender.permissions.SimplePermission;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.annotations.methods.OnEnable;

import java.util.Map;

/**
 * <p>A class representing the alerts command.</p>
 */
@Service(dependsOn = {
        PunishmentManagerImpl.class,
        TranslationManagerImpl.class
})
public class AlertsCommand extends BaseCommand {
    /**
     * <p>Boolean to on/off abbreviations.</p>
     */
    private static final Map<Boolean, String> BOOL_ABBR = Map.of(true, "on", false, "off");
    private TranslationManager translationManager;
    private PunishmentManager punishmentManager;

    /**
     * <p>Constructs the command.</p>
     */
    public AlertsCommand() {
        super("alerts", SimplePermission.of("iris.alerts"), false);
    }

    @OnEnable
    public void enable(TranslationManagerImpl translationManager, PunishmentManagerImpl punishmentManager) {
        this.translationManager = translationManager;
        this.punishmentManager = punishmentManager;
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
                            final boolean now = punishmentManager.toggleAlerts((PlayerWrapper) commandContext.getSender());
                            commandContext.getSender().sendMessage(
                                    translationManager.getMessage("alertsToggle", Map.of("status", BOOL_ABBR.get(now)), commandContext.getSender().getLocale())
                            );
                        })
        );
    }
}

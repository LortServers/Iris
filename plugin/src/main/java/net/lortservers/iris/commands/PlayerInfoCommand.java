package net.lortservers.iris.commands;

import cloud.commandframework.Command;
import cloud.commandframework.CommandManager;
import cloud.commandframework.bukkit.parsers.PlayerArgument;
import net.lortservers.iris.managers.ConfigurationManager;
import net.lortservers.iris.managers.PunishmentManager;
import net.lortservers.iris.utils.Protocol;
import net.lortservers.iris.utils.ProtocolUtils;
import org.screamingsandals.lib.sender.CommandSenderWrapper;
import org.screamingsandals.lib.sender.permissions.SimplePermission;
import org.screamingsandals.lib.utils.annotations.Service;

import java.util.Map;
import java.util.Optional;

/**
 * <p>A class representing the alerts command.</p>
 */
@Service(dependsOn = {
        PunishmentManager.class
})
public class PlayerInfoCommand extends BaseCommand {
    /**
     * <p>Constructs the command.</p>
     */
    public PlayerInfoCommand() {
        super("player", SimplePermission.of("iris.playerinfo"), true);
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
                        .argument(PlayerArgument.of("player"))
                        .handler(commandContext -> {
                            final Optional<Protocol> proto = ProtocolUtils.getProtocol(756);
                            final String protocolString = (proto.isPresent()) ? proto.orElseThrow().getVersion() + " (" + proto.orElseThrow().getMinecraftVersion() + ")" : "Unknown";
                            commandContext.getSender().sendMessage(
                                    ConfigurationManager.getInstance().getMessage("playerInfo", Map.of("protocol", protocolString))
                            );
                        })
        );
    }
}

package net.lortservers.iris.commands;

import cloud.commandframework.Command;
import cloud.commandframework.CommandManager;
import cloud.commandframework.bukkit.parsers.PlayerArgument;
import net.lortservers.iris.api.managers.ConfigurationManager;
import net.lortservers.iris.api.managers.PunishmentManager;
import net.lortservers.iris.utils.protocol.Protocol;
import net.lortservers.iris.utils.protocol.ProtocolUtils;
import org.screamingsandals.lib.sender.CommandSenderWrapper;
import org.screamingsandals.lib.sender.permissions.SimplePermission;
import org.screamingsandals.lib.utils.annotations.Service;

import java.util.Map;

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
                            final Protocol proto = ProtocolUtils.getPlayerProtocol(commandContext.get("player"));
                            final String protocolString = proto.getVersion() + " (" + proto.getMinecraftVersion() + ")";
                            commandContext.getSender().sendMessage(
                                    ConfigurationManager.getInstance().getMessage("playerInfo", Map.of("protocol", protocolString))
                            );
                        })
        );
    }
}

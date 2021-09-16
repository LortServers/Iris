package net.lortservers.iris.commands;

import cloud.commandframework.CommandManager;
import cloud.commandframework.execution.CommandExecutionCoordinator;
import cloud.commandframework.minecraft.extras.MinecraftExceptionHandler;
import lombok.experimental.UtilityClass;
import net.lortservers.iris.config.ConfigurationManager;
import org.screamingsandals.lib.command.CloudConstructor;
import org.screamingsandals.lib.plugin.ServiceManager;
import org.screamingsandals.lib.sender.CommandSenderWrapper;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.annotations.methods.Provider;

/**
 * <p>Class providing the command manager.</p>
 */
@Service(dependsOn = {
        CloudConstructor.class,
        ConfigurationManager.class
})
@UtilityClass
public class CommandService {
    /**
     * <p>Provides the command manager.</p>
     *
     * @return the command manager
     */
    @Provider(level = Provider.Level.POST_ENABLE)
    public static CommandManager<CommandSenderWrapper> provideCommandManager() {
        try {
            var manager = CloudConstructor.construct(CommandExecutionCoordinator.simpleCoordinator());

            new MinecraftExceptionHandler<CommandSenderWrapper>()
                    .withDefaultHandlers()
                    .withHandler(MinecraftExceptionHandler.ExceptionType.NO_PERMISSION, (senderWrapper, e) ->
                            ServiceManager.get(ConfigurationManager.class).getMessage("noPermission")
                    )
                    .withHandler(MinecraftExceptionHandler.ExceptionType.INVALID_SYNTAX, (senderWrapper, e) ->
                            ServiceManager.get(ConfigurationManager.class).getMessage("invalidCommand")
                    )
                    .apply(manager, s -> s);

            return manager;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

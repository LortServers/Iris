package net.lortservers.iris.commands;

import cloud.commandframework.CommandManager;
import cloud.commandframework.execution.CommandExecutionCoordinator;
import cloud.commandframework.minecraft.extras.MinecraftExceptionHandler;
import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.lortservers.iris.config.Configurator;
import org.screamingsandals.lib.command.CloudConstructor;
import org.screamingsandals.lib.plugin.ServiceManager;
import org.screamingsandals.lib.sender.CommandSenderWrapper;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.annotations.methods.Provider;

@Service(dependsOn = {
        CloudConstructor.class,
        Configurator.class
})
@UtilityClass
public class CommandService {
    @Provider(level = Provider.Level.POST_ENABLE)
    public static CommandManager<CommandSenderWrapper> provideCommandManager() {
        try {
            var manager = CloudConstructor.construct(CommandExecutionCoordinator.simpleCoordinator());

            new MinecraftExceptionHandler<CommandSenderWrapper>()
                    .withDefaultHandlers()
                    .withHandler(MinecraftExceptionHandler.ExceptionType.NO_PERMISSION, (senderWrapper, e) ->
                            ServiceManager.get(Configurator.class).getMessages().getMessage("noPermission")
                    )
                    .withHandler(MinecraftExceptionHandler.ExceptionType.INVALID_SYNTAX, (senderWrapper, e) ->
                            ServiceManager.get(Configurator.class).getMessages().getMessage("invalidCommand")
                    )
                    .apply(manager, s -> s);

            return manager;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

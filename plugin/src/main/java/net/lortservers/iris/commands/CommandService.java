package net.lortservers.iris.commands;

import cloud.commandframework.CommandManager;
import cloud.commandframework.execution.CommandExecutionCoordinator;
import cloud.commandframework.minecraft.extras.MinecraftExceptionHandler;
import lombok.experimental.UtilityClass;
import net.lortservers.iris.api.managers.TranslationManager;
import net.lortservers.iris.config.ConfigurationManagerImpl;
import net.lortservers.iris.config.TranslationManagerImpl;
import org.screamingsandals.lib.command.CloudConstructor;
import org.screamingsandals.lib.sender.CommandSenderWrapper;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.annotations.methods.Provider;

/**
 * <p>Class providing the command manager.</p>
 */
@Service(dependsOn = {
        ConfigurationManagerImpl.class,
        TranslationManagerImpl.class,
        CloudConstructor.class
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
                            TranslationManager.getInstance().getMessage("noPermission", senderWrapper.getLocale())
                    )
                    .withHandler(MinecraftExceptionHandler.ExceptionType.INVALID_SYNTAX, (senderWrapper, e) ->
                            TranslationManager.getInstance().getMessage("invalidCommand", senderWrapper.getLocale())
                    )
                    .apply(manager, s -> s);

            return manager;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

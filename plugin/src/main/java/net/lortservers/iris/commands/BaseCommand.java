package net.lortservers.iris.commands;

import cloud.commandframework.Command;
import cloud.commandframework.CommandManager;
import cloud.commandframework.keys.SimpleCloudKey;
import cloud.commandframework.permission.PredicatePermission;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.lortservers.iris.wrap.ConfigurationDependent;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.sender.CommandSenderWrapper;
import org.screamingsandals.lib.sender.permissions.Permission;
import org.screamingsandals.lib.utils.annotations.ServiceDependencies;
import org.screamingsandals.lib.utils.annotations.methods.OnPostEnable;
import org.screamingsandals.lib.utils.annotations.parameters.ProvidedBy;

/**
 * <p>Command base class.</p>
 */
@RequiredArgsConstructor
@Getter
@ServiceDependencies(dependsOn = {
        CommandService.class
})
public abstract class BaseCommand extends ConfigurationDependent {
    /**
     * <p>Command name.</p>
     */
    protected final @NonNull String name;
    /**
     * <p>Command permission.</p>
     */
    protected final @Nullable Permission permission;
    /**
     * <p>Can the command be used in console?</p>
     */
    protected final boolean allowConsole;

    /**
     * <p>Constructs the command behavior.</p>
     *
     * @param commandSenderWrapperBuilder the command builder
     * @param manager the command manager
     */
    protected abstract void construct(Command.Builder<CommandSenderWrapper> commandSenderWrapperBuilder, CommandManager<CommandSenderWrapper> manager);

    /**
     * <p>Constructs the command pre-execute checks.</p>
     *
     * @param manager the command manager
     */
    @OnPostEnable
    public void construct(@ProvidedBy(CommandService.class) CommandManager<CommandSenderWrapper> manager) {
        var builder = manager.commandBuilder("iris")
                .literal(name);
        if (permission != null) {
            builder = builder.permission(
                    PredicatePermission.of(SimpleCloudKey.of(name), perm ->
                            perm.getType() == CommandSenderWrapper.Type.CONSOLE || permission.hasPermission(perm)
                    )
            );
        }
        if (!allowConsole) {
            builder = builder.senderType(PlayerWrapper.class);
        }
        construct(builder, manager);
    }
}

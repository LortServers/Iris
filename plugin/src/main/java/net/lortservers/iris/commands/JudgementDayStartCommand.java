package net.lortservers.iris.commands;

import cloud.commandframework.Command;
import cloud.commandframework.CommandManager;
import net.lortservers.iris.api.managers.ConfigurationManager;
import net.lortservers.iris.utils.profiles.PlayerProfileManager;
import org.screamingsandals.lib.sender.CommandSenderWrapper;
import org.screamingsandals.lib.sender.permissions.SimplePermission;
import org.screamingsandals.lib.utils.annotations.Service;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Service(dependsOn = {
        PlayerProfileManager.class
})
public class JudgementDayStartCommand extends BaseCommand {
    public JudgementDayStartCommand() {
        super("jd", SimplePermission.of("iris.judgementday.start"), true);
    }

    @Override
    protected void construct(Command.Builder<CommandSenderWrapper> commandSenderWrapperBuilder, CommandManager<CommandSenderWrapper> manager) {
        manager.command(
                commandSenderWrapperBuilder
                        .handler(commandContext -> {
                            final AtomicInteger count = new AtomicInteger(0);
                            PlayerProfileManager.modifyAll(e -> {
                                e.setJudgementDay(false);
                                // TODO: finish this
                                count.getAndIncrement();
                                return e;
                            }).thenRun(() -> commandContext.getSender().sendMessage(
                                    ConfigurationManager.getInstance().getMessage(
                                            "judgementDayComplete",
                                            Map.of("count", count.toString())
                                    )
                            ));
                        })
        );
    }
}

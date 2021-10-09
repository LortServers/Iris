package net.lortservers.iris.commands;

import cloud.commandframework.Command;
import cloud.commandframework.CommandManager;
import net.lortservers.iris.api.managers.ConfigurationManager;
import net.lortservers.iris.api.managers.PunishmentManager;
import net.lortservers.iris.utils.profiles.PlayerProfileManager;
import org.screamingsandals.lib.sender.CommandSenderWrapper;
import org.screamingsandals.lib.sender.permissions.SimplePermission;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.player.PlayerWrapper;

import java.util.Optional;
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
                        .literal("start")
                        .handler(commandContext -> {
                            final AtomicInteger count = new AtomicInteger(0);
                            final String cheatMessage = ConfigurationManager.getInstance().getRawMessage("banMessageCheating");
                            PlayerProfileManager.modifyAll(e -> {
                                if (e.isJudgementDay()) {
                                    e.setJudgementDay(false);
                                    e.setBanMessage(cheatMessage);
                                    final Optional<PlayerWrapper> player = e.toPlayer();
                                    if (player.isPresent()) {
                                        PunishmentManager.getInstance().kick(player.orElseThrow(), cheatMessage);
                                    }
                                    count.getAndIncrement();
                                    return e;
                                }
                                return null;
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

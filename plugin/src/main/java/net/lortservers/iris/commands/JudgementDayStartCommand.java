package net.lortservers.iris.commands;

import cloud.commandframework.Command;
import cloud.commandframework.CommandManager;
import net.lortservers.iris.api.managers.PunishmentManager;
import net.lortservers.iris.api.managers.TranslationManager;
import net.lortservers.iris.config.TranslationManagerImpl;
import net.lortservers.iris.utils.PunishmentManagerImpl;
import net.lortservers.iris.utils.profiles.PlayerProfileManager;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.sender.CommandSenderWrapper;
import org.screamingsandals.lib.sender.permissions.SimplePermission;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.annotations.methods.OnEnable;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Service(dependsOn = {
        PlayerProfileManager.class,
        TranslationManagerImpl.class,
        PunishmentManagerImpl.class
})
public class JudgementDayStartCommand extends BaseCommand {
    private PunishmentManager punishmentManager;
    private TranslationManager translationManager;

    public JudgementDayStartCommand() {
        super("jd", SimplePermission.of("iris.judgementday.start"), true);
    }

    @OnEnable
    public void enable(TranslationManagerImpl translationManager, PunishmentManagerImpl punishmentManager) {
        this.translationManager = translationManager;
        this.punishmentManager = punishmentManager;
    }

    @Override
    protected void construct(Command.Builder<CommandSenderWrapper> commandSenderWrapperBuilder, CommandManager<CommandSenderWrapper> manager) {
        manager.command(
                commandSenderWrapperBuilder
                        .literal("start")
                        .handler(commandContext -> {
                            final AtomicInteger count = new AtomicInteger(0);
                            final String cheatMessage = translationManager.getRawMessage("banMessageCheating");
                            PlayerProfileManager.modifyAll(e -> {
                                if (e.isJudgementDay()) {
                                    e.setJudgementDay(false);
                                    e.setBanMessage(cheatMessage);
                                    final Optional<PlayerWrapper> player = e.toPlayer();
                                    if (player.isPresent()) {
                                        punishmentManager.kick(player.orElseThrow(), cheatMessage);
                                    }
                                    count.getAndIncrement();
                                    return e;
                                }
                                return null;
                            }).thenRun(() -> commandContext.getSender().sendMessage(
                                    translationManager.getMessage(
                                            "judgementDayComplete",
                                            Map.of("count", count.toString()),
                                            commandContext.getSender().getLocale()
                                    )
                            ));
                        })
        );
    }
}

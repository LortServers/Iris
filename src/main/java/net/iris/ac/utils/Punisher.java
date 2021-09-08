package net.iris.ac.utils;

import net.iris.ac.checks.Check;
import net.iris.ac.config.Configurator;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.annotations.Service;

@Service(dependsOn = {
        Configurator.class,
        PlayerMapper.class
})
public class Punisher {
    public <T extends Check> void logWarn(PlayerWrapper player, T check) {
        final TextComponent component = Component.text()
                .append(Component.text("(", NamedTextColor.GRAY))
                .append(Component.text("!", NamedTextColor.RED))
                .append(Component.text(") ", NamedTextColor.GRAY))
                .append(Component.text(player.getName(), NamedTextColor.RED))
                .append(Component.text(" failed ", NamedTextColor.WHITE))
                .append(Component.text(check.getName() + " " + check.getType().name(), NamedTextColor.GOLD))
                .append(Component.text(" | ", NamedTextColor.GRAY))
                .append(Component.text("VL: ", NamedTextColor.BLUE))
                .append(Component.text(check.getVL(player)))
                .build();
        PlayerMapper.getPlayers().stream().filter(e -> e.hasPermission("iris.alerts")).forEach(e -> e.sendMessage(component));
    }
}

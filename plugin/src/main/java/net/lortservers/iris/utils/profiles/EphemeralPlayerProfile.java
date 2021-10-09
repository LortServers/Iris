package net.lortservers.iris.utils.profiles;

import lombok.*;
import lombok.experimental.Accessors;
import net.lortservers.iris.api.checks.Check;
import net.lortservers.iris.api.utils.IntegerPair;
import net.lortservers.iris.utils.CooldownMapping;
import net.lortservers.iris.utils.misc.IntegerPairImpl;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.player.PlayerWrapper;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Accessors(chain = true)
public class EphemeralPlayerProfile {
    @Setter(AccessLevel.PROTECTED)
    private UUID player = null;
    private final Map<Class<? extends Check>, Integer> checkVLs = new HashMap<>();
    private final Map<Class<? extends Check>, CooldownMapping> checkCooldowns = new HashMap<>();
    private final IntegerPair cps = IntegerPairImpl.of(0, 0);
    private long lastBreak = 0;
    private int aimbotCounter = 0;
    private boolean alertSubscriber = false;

    public static EphemeralPlayerProfile of(UUID player) {
        return new EphemeralPlayerProfile().setPlayer(player);
    }

    public Optional<PlayerWrapper> toPlayer() {
        return PlayerMapper.getPlayer(player);
    }
}

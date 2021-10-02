package net.lortservers.iris.utils.profiles;

import lombok.Data;
import net.lortservers.iris.checks.Check;
import net.lortservers.iris.utils.CooldownMapping;
import net.lortservers.iris.utils.IntegerPair;
import net.lortservers.iris.utils.misc.IntegerPairImpl;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.player.PlayerWrapper;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Data(staticConstructor = "of")
public class EphemeralPlayerProfile {
    private final UUID player;
    private final Map<Class<? extends Check>, Integer> checkVLs = new HashMap<>();
    private final Map<Class<? extends Check>, CooldownMapping> checkCooldowns = new HashMap<>();
    private final IntegerPair cps = IntegerPairImpl.of(0, 0);
    private long lastBreak = 0;
    private int aimbotCounter = 0;
    private boolean alertSubscriber = false;

    public PlayerWrapper toPlayer() {
        return PlayerMapper.wrapPlayer(player);
    }
}

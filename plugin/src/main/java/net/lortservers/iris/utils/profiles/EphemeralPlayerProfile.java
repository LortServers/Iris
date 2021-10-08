package net.lortservers.iris.utils.profiles;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.lortservers.iris.api.checks.Check;
import net.lortservers.iris.api.profiles.AbstractPlayerProfile;
import net.lortservers.iris.utils.CooldownMapping;
import net.lortservers.iris.api.utils.IntegerPair;
import net.lortservers.iris.utils.misc.IntegerPairImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = false)
public class EphemeralPlayerProfile extends AbstractPlayerProfile {
    private final Map<Class<? extends Check>, Integer> checkVLs = new HashMap<>();
    private final Map<Class<? extends Check>, CooldownMapping> checkCooldowns = new HashMap<>();
    private final IntegerPair cps = IntegerPairImpl.of(0, 0);
    private long lastBreak = 0;
    private int aimbotCounter = 0;
    private boolean alertSubscriber = false;

    private EphemeralPlayerProfile(UUID player) {
        super(player);
    }

    public static EphemeralPlayerProfile of(UUID player) {
        return new EphemeralPlayerProfile(player);
    }
}

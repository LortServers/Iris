package net.lortservers.iris.utils.profiles.persistence;

import lombok.Data;
import net.lortservers.iris.utils.profiles.PlayerProfileManager;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.player.PlayerWrapper;

import java.util.UUID;

@Data(staticConstructor = "of")
public class PersistentPlayerProfile {
    private final UUID player;
    private boolean judgementDay = false;

    public PlayerWrapper toPlayer() {
        return PlayerMapper.wrapPlayer(player);
    }

    public void save() {
        PlayerProfileManager.persist(this);
    }
}

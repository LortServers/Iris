package net.lortservers.iris.profiles;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.player.PlayerWrapper;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public abstract class AbstractPlayerProfile {
    private final UUID player;

    public PlayerWrapper toPlayer() {
        return PlayerMapper.wrapPlayer(player);
    }
}

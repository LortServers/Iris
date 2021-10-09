package net.lortservers.iris.api.profiles;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.player.PlayerWrapper;

import java.util.Optional;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public abstract class AbstractPlayerProfile {
    private final UUID player;

    public Optional<PlayerWrapper> toPlayer() {
        return PlayerMapper.getPlayer(player);
    }
}

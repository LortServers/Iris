package net.lortservers.iris.utils.profiles.persistence;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.Accessors;
import net.lortservers.iris.utils.profiles.PlayerProfileManager;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.player.PlayerWrapper;

import java.util.Optional;
import java.util.UUID;

@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Accessors(chain = true)
public class PersistentPlayerProfile implements AutoCloseable {
    @Setter(AccessLevel.PRIVATE)
    private UUID player = null;
    private boolean judgementDay = false;
    private String banMessage = null;

    public static PersistentPlayerProfile of(UUID player) {
        return new PersistentPlayerProfile().setPlayer(player);
    }

    @JsonIgnore
    public boolean isBanned() {
        return banMessage != null;
    }

    public Optional<PlayerWrapper> toPlayer() {
        return PlayerMapper.getPlayer(player);
    }

    @Override
    public void close() {
        PlayerProfileManager.persist(this);
    }
}

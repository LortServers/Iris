package net.lortservers.iris.utils.profiles.persistence;

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
public class PersistentPlayerProfile {
    @Setter(AccessLevel.PRIVATE)
    private UUID player = null;
    private boolean judgementDay = false;
    private String banMessage = null;

    public static PersistentPlayerProfile of(UUID player) {
        return new PersistentPlayerProfile().setPlayer(player);
    }

    public void save() {
        PlayerProfileManager.persist(this);
    }

    public boolean isBanned() {
        return banMessage != null;
    }

    public Optional<PlayerWrapper> toPlayer() {
        return PlayerMapper.getPlayer(player);
    }
}

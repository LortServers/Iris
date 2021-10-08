package net.lortservers.iris.utils.profiles.persistence;

import lombok.*;
import net.lortservers.iris.api.profiles.AbstractPlayerProfile;
import net.lortservers.iris.utils.profiles.PlayerProfileManager;

import java.util.UUID;

@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = false)
public class PersistentPlayerProfile extends AbstractPlayerProfile {
    private boolean judgementDay = false;
    private String banMessage = null;

    private PersistentPlayerProfile(UUID player) {
        super(player);
    }

    public static PersistentPlayerProfile of(UUID player) {
        return new PersistentPlayerProfile(player);
    }

    public void save() {
        PlayerProfileManager.persist(this);
    }

    public boolean isBanned() {
        return banMessage != null;
    }
}

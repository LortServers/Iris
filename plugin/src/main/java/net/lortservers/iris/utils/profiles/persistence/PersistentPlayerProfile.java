package net.lortservers.iris.utils.profiles.persistence;

import lombok.*;
import net.lortservers.iris.profiles.AbstractPlayerProfile;
import net.lortservers.iris.utils.profiles.PlayerProfileManager;

import java.util.UUID;

@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = false)
public class PersistentPlayerProfile extends AbstractPlayerProfile {
    private boolean judgementDay = false;

    private PersistentPlayerProfile(UUID player) {
        super(player);
    }

    public static PersistentPlayerProfile of(UUID player) {
        return new PersistentPlayerProfile(player);
    }

    public void save() {
        PlayerProfileManager.persist(this);
    }
}

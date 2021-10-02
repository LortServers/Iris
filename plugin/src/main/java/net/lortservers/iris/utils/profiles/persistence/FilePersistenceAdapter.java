package net.lortservers.iris.utils.profiles.persistence;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import net.lortservers.iris.IrisPlugin;
import net.lortservers.iris.config.ConfigurationManagerImpl;
import org.screamingsandals.lib.event.EventManager;
import org.screamingsandals.lib.event.player.SPlayerLeaveEvent;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class FilePersistenceAdapter implements PersistenceAdapter<PersistentPlayerProfile> {
    private static final Path profileFolder = Paths.get(IrisPlugin.getInstance().getDataFolder().toAbsolutePath().toString(), "profiles");
    private final Map<UUID, PersistentPlayerProfile> profileCache = new ConcurrentHashMap<>();

    public FilePersistenceAdapter() {
        EventManager.getDefaultEventManager().register(SPlayerLeaveEvent.class, event -> profileCache.remove(event.getPlayer().getUuid()));
    }

    @Override
    public void persist(PersistentPlayerProfile profile) {
        if (!profileFolder.toFile().exists()) {
            //noinspection ResultOfMethodCallIgnored
            profileFolder.toFile().mkdirs();
        }
        profileCache.put(profile.getPlayer(), profile);
        final File profileFile = Paths.get(profileFolder.toString(), profile.getPlayer() + ".json").toFile();
        if (profileFile.exists()) {
            //noinspection ResultOfMethodCallIgnored
            profileFile.delete();
        }
        CompletableFuture.runAsync(() -> {
            try {
                ConfigurationManagerImpl.MAPPER.writeValue(profileFile, PersistentPlayerProfile.class);
            } catch (IOException e) {
                IrisPlugin.getInstance().getLogger().error("Could not persist player profile: " + profile.getPlayer(), e);
            }
        });
    }

    @Override
    public CompletableFuture<PersistentPlayerProfile> retrieve(UUID player) {
        return CompletableFuture.supplyAsync(() -> {
            if (profileCache.containsKey(player)) {
                return profileCache.get(player);
            }
            final File profile = Paths.get(profileFolder.toString(), player.toString() + ".json").toFile();
            try {
                if (profile.exists()) {
                    final PersistentPlayerProfile result = ConfigurationManagerImpl.MAPPER.readValue(profile, PersistentPlayerProfile.class);
                    profileCache.put(player, result);
                    return result;
                }
            } catch (IOException e) {
                IrisPlugin.getInstance().getLogger().error("Could not retrieve player profile: " + player, e);
            }
            return PersistentPlayerProfile.of(player);
        });
    }

    @Override
    public CompletableFuture<List<PersistentPlayerProfile>> all() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return Lists.newArrayList(
                        Iterables.concat(
                                Files.walk(profileFolder)
                                        .map(Path::toFile)
                                        .filter(e -> e.getName().endsWith(".json") && e.isFile())
                                        .filter(e -> !profileCache.containsKey(UUID.fromString(e.getName().replace(".json", ""))))
                                        .map(e -> {
                                            try {
                                                return ConfigurationManagerImpl.MAPPER.readValue(e, PersistentPlayerProfile.class);
                                            } catch (IOException ex) {
                                                IrisPlugin.getInstance().getLogger().error("Could not retrieve player profile: " + e.getName(), ex);
                                            }
                                            return null;
                                        })
                                        .filter(Objects::nonNull)
                                        .toList(),
                                profileCache.values()
                        )
                );
            } catch (IOException e) {
                IrisPlugin.getInstance().getLogger().error("Could not read player profiles, using only cached ones.", e);
                return Lists.newArrayList(profileCache.values());
            }
        });
    }
}

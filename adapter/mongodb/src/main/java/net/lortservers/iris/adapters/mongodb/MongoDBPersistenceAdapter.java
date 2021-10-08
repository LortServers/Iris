package net.lortservers.iris.adapters.mongodb;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import net.lortservers.iris.IrisPlugin;
import net.lortservers.iris.utils.profiles.persistence.PersistenceAdapter;
import net.lortservers.iris.utils.profiles.persistence.PersistentPlayerProfile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class MongoDBPersistenceAdapter implements PersistenceAdapter<PersistentPlayerProfile> {
    private final MongoCollection<PersistentPlayerProfile> usersColl;

    public MongoDBPersistenceAdapter(MongoDatabase db) {
        usersColl = db.getCollection("users", PersistentPlayerProfile.class);
    }

    @Override
    public void persist(PersistentPlayerProfile profile) {
        CompletableFuture.runAsync(() -> {
            if (usersColl.find(Filters.eq("player", profile.getPlayer())).first() != null) {
                usersColl.replaceOne(Filters.eq("player", profile.getPlayer()), profile);
            } else {
                usersColl.insertOne(profile);
            }
        }, IrisPlugin.THREAD_POOL);
    }

    @Override
    public CompletableFuture<PersistentPlayerProfile> retrieve(UUID player) {
        return CompletableFuture.supplyAsync(() -> Objects.requireNonNullElse(usersColl.find(Filters.eq("player", player)).first(), PersistentPlayerProfile.of(player)), IrisPlugin.THREAD_POOL);
    }

    @Override
    public CompletableFuture<List<PersistentPlayerProfile>> all() {
        return CompletableFuture.supplyAsync(() -> usersColl.find().into(new ArrayList<>()), IrisPlugin.THREAD_POOL);
    }
}

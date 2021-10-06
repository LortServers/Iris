package net.lortservers.iris.adapters.mongodb;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.RequiredArgsConstructor;
import net.lortservers.iris.utils.profiles.persistence.PersistenceAdapter;
import net.lortservers.iris.utils.profiles.persistence.PersistentPlayerProfile;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public class MongoDBPersistenceAdapter implements PersistenceAdapter<PersistentPlayerProfile> {
    private final MongoDatabase database;

    @Override
    public void persist(PersistentPlayerProfile profile) {
        final MongoCollection<PersistentPlayerProfile> coll = database.getCollection("users", PersistentPlayerProfile.class);
        coll.findOneAndDelete(new BasicDBObject("player", profile.getPlayer()));
        coll.insertOne(profile);
    }

    @Override
    public CompletableFuture<PersistentPlayerProfile> retrieve(UUID player) {
        return null;
    }

    @Override
    public CompletableFuture<List<PersistentPlayerProfile>> all() {
        return null;
    }
}

package net.lortservers.iris.utils.profiles.persistence;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface PersistenceAdapter<T> {
    void persist(T profile);
    CompletableFuture<T> retrieve(UUID player);
    CompletableFuture<List<T>> all();
}

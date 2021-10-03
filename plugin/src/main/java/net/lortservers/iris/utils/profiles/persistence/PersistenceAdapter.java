package net.lortservers.iris.utils.profiles.persistence;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.screamingsandals.lib.Server;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public interface PersistenceAdapter<T> {
    void persist(T profile);
    CompletableFuture<T> retrieve(UUID player);
    CompletableFuture<List<T>> all();
    default CompletableFuture<List<T>> fromOnline() {
        return CompletableFuture.supplyAsync(() -> Server.getConnectedPlayers().stream()
                .map(e -> retrieve(e.getUuid()).join())
                .toList()
        );
    }
    default CompletableFuture<Void> modify(UUID player, Function<@NonNull T, @NonNull T> func) {
        return retrieve(player).thenAcceptAsync(e -> persist(func.apply(e)));
    }
    default CompletableFuture<Void> modifyAll(Function<@NonNull T, @NonNull T> func) {
        return all().thenAcceptAsync(e -> {
            for (T profile : e) {
                persist(func.apply(profile));
            }
        });
    }
}

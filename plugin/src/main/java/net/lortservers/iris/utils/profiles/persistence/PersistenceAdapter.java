package net.lortservers.iris.utils.profiles.persistence;

import net.lortservers.iris.IrisPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
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
        final List<CompletableFuture<T>> futures = Server.getConnectedPlayers().stream().map(e -> retrieve(e.getUuid())).toList();
        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new))
                .thenApplyAsync(i -> futures.stream().map(CompletableFuture::join).toList(), IrisPlugin.THREAD_POOL);
    }
    default CompletableFuture<Void> modify(UUID player, Function<@NonNull T, @NonNull T> func) {
        return retrieve(player).thenAcceptAsync(e -> persist(func.apply(e)), IrisPlugin.THREAD_POOL);
    }
    default CompletableFuture<Void> modifyAll(Function<@NonNull T, @Nullable T> func) {
        return all().thenAcceptAsync(e -> {
            for (T profile : e) {
                final T result = func.apply(profile);
                if (result != null) {
                    persist(result);
                }
            }
        }, IrisPlugin.THREAD_POOL);
    }
}

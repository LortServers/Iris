package net.lortservers.iris.utils.tasks;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public final class ThreadedExecutor {
    private static final ExecutorService executor = Executors.newCachedThreadPool();

    public static Future<?> executeTask(Runnable task) {
        return executor.submit(task);
    }
}

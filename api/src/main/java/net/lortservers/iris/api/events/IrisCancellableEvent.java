package net.lortservers.iris.api.events;

public interface IrisCancellableEvent {
    boolean isCancelled();

    void setCancelled(boolean cancelled);
}
